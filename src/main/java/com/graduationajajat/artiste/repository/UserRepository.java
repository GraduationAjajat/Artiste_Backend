package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Authority;
import com.graduationajajat.artiste.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String username);

    // 이메일로 유저 정보 조회
    Optional<User> findByEmail(String email);

    //이메일 중복 체크
    boolean existsByEmail(String email);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

    // 관리자 아닌 회원 목록 조회
    @EntityGraph(attributePaths = "authorities")
    List<User> findAllByAuthoritiesIn(Set<Authority> singleton);
}
