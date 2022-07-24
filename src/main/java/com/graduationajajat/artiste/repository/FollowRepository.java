package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 사용자 팔로워 목록 조회
    List<Follow> findAllByFollowingId(Long followingId);

    // 사용자 팔로잉 목록 조회
    List<Follow> findAllByFollowerId(Long followerId);

    // 사용자 팔로워/팔로워 삭제
    void deleteByFollowingIdAndFollowerId(Long followingId, Long followerId);

    // 사용자 팔로잉 여부
    boolean existsByFollowingIdAndFollowerId(Long followerId, Long followingId);
}