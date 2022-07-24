package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.model.Exhibition;
import com.graduationajajat.artiste.model.Follow;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.repository.FollowRepository;
import com.graduationajajat.artiste.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 사용자 팔로워 목록 조회
    @Transactional
    public List<User> getFollowersByFollowingId(Long userId) {
        List<Follow> followList = followRepository.findAllByFollowingId(userId);
        List<User> followerList = new ArrayList<>();
        for(Follow follow : followList) {
            followerList.add(follow.getFollower());
        }
        return followerList;
    }

    // 사용자 팔로잉 목록 조회
    @Transactional
    public List<User> getFollowingsByFollowerId(Long userId) {
        List<Follow> followList = followRepository.findAllByFollowerId(userId);
        List<User> followerList = new ArrayList<>();
        for(Follow follow : followList) {
            followerList.add(follow.getFollowing());
        }
        return followerList;
    }

    // 사용자 팔로잉 추가
    @Transactional
    public void createFollowing(User user, Long followingId) {
        User Following = userRepository.findById(followingId).get();
        Follow follow = Follow.builder().following(Following).follower(user).build();

        followRepository.save(follow);
    }

    // 사용자 팔로워/팔로잉 삭제
    @Transactional
    public void deleteFollow(Long followingId, Long followerId) {
        followRepository.deleteByFollowingIdAndFollowerId(followingId, followerId);
    }

    // 사용자 팔로잉 여부
    @Transactional(readOnly = true)
    public boolean checkUserFollowing(Long userId, Long followingId) {
        return followRepository.existsByFollowingIdAndFollowerId(followingId, userId);
    }
}
