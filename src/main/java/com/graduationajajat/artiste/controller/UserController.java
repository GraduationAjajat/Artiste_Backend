package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.request.LoginDto;
import com.graduationajajat.artiste.dto.request.TokenDto;
import com.graduationajajat.artiste.dto.request.TokenRequestDto;
import com.graduationajajat.artiste.dto.request.UserDto;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@Api(tags = {"User API"})
public class UserController {

    private final UserService userService;

    // 로그인 Controller
    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @ApiOperation(value = "토큰 재발행")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }

    // 회원가입 Controller
    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto, "ROLE_USER"));
    }

    // 관리자 회원가입 Controller
    @ApiOperation(value = "관리자 회원가입")
    @PostMapping("/admin/signup")
    public ResponseEntity<Object> signupAdmin(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto, "ROLE_ADMIN"));
    }

    // 아이디 중복 확인 Controller (존재하면 true)
    @ApiOperation(value = "아이디 중복 확인")
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkIdDuplication(email));
    }

    // 닉네임 중복 확인 Controller (존재하면 true)
    @ApiOperation(value = "닉네임 중복 확인")
    @GetMapping("/check/nickname/{nickname}")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplication(nickname));
    }


    // SpringContext 에서 유저 정보 조회 Controller
    @ApiOperation(value = "유저 정보 조회 - SpringContext 내")
    @GetMapping("")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyInfo());
    }

    // 이메일로 유저 정보 조회 Controller
    @ApiOperation(value = "유저 정보 조회 - 이메일")
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserInfo(email));
    }

    // 회원 정보 수정 Controller
    @ApiOperation(value = "회원 정보 수정")
    @PostMapping("")
    public ResponseEntity<Object> update(@Valid @RequestPart UserDto userDto, @Nullable  @RequestPart MultipartFile profileImage) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(userService.update(user, userDto, profileImage));
    }

}
