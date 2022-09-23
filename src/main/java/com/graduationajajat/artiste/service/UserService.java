package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.dto.request.LoginDto;
import com.graduationajajat.artiste.dto.request.TokenDto;
import com.graduationajajat.artiste.dto.request.TokenRequestDto;
import com.graduationajajat.artiste.dto.request.UserDto;
import com.graduationajajat.artiste.dto.response.UserResponseDto;
import com.graduationajajat.artiste.jwt.TokenProvider;
import com.graduationajajat.artiste.model.Authority;
import com.graduationajajat.artiste.model.FileFolder;
import com.graduationajajat.artiste.model.RefreshToken;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.repository.CommentRepository;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import com.graduationajajat.artiste.repository.RefreshTokenRepository;
import com.graduationajajat.artiste.repository.UserRepository;
import com.graduationajajat.artiste.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final CommentRepository commentRepository;
    private final FileProcessService fileProcessService;

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    // 회원가입
    @Transactional
    public User signup(UserDto userDto, String role) {

        Authority authority = Authority.builder()
                .authorityName(role)
                .build();

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .birthday(userDto.getBirthday())
                .gender(userDto.getGender())
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    // 유저 이메일 중복 체크
    @Transactional(readOnly = true)
    public boolean checkIdDuplication(String email) {
        return userRepository.existsByEmail(email);
    }

    // 유저 닉네임 중복 체크
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplication(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 현재 SecurityContext 에 있는 유저 정보 조회
    @Transactional(readOnly = true)
    public User getMyInfo() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByEmail).get();
    }

    // 회원 정보 수정
    @Transactional
    public Object update(User user, UserDto userDto) {

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setNickname(userDto.getNickname());
        user.setBirthday(userDto.getBirthday());
        user.setGender(userDto.getGender());
        String url = fileProcessService.uploadImage(userDto.getProfileImage(), FileFolder.PROFILE_IMAGES);
        user.setProfileImage(url);

        return userRepository.save(user);
    }

    // 이메일로 유저 정보 조회
    @Transactional(readOnly = true)
    public User getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 관리자 - 회원 관리 목록 조회
    public List<UserResponseDto> getUsers() {
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        List<User> userList = userRepository.findAllByAuthoritiesIn(Collections.singleton(authority));
        for(User user : userList) {
            Long exhibitionCount = exhibitionRepository.countByUserId(user.getId());
            Long commentCount = commentRepository.countByUserId(user.getId());
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .registerDate(user.getCreatedDate())
                    .exhibitionCount(exhibitionCount)
                    .commentCount(commentCount).build();
            userResponseDtoList.add(userResponseDto);
        }

        return userResponseDtoList;
    }
}
