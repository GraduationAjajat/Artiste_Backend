package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 유저 정보 조회
    Optional<User> findByEmail(String email);

    //이메일 중복 체크
    boolean existsByEmail(String email);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);
}
