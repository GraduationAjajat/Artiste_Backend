package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.CommonResponseDto;
import com.graduationajajat.artiste.dto.ResponseDto;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.service.FollowService;
import com.graduationajajat.artiste.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/follow")
@Api(tags = {"Follow API"})
public class FollowController {

    private final UserService userService;
    private final FollowService followService;

    // 사용자 팔로워 목록 조회
    @ApiOperation(value = "사용자 팔로워 목록 조회")
    @GetMapping("/follower")
    public ResponseEntity<? extends ResponseDto> getFollowersByFollowingId() {
        User user = userService.getMyInfo();
        List<User> followerList = followService.getFollowersByFollowingId(user.getId());
        return ResponseEntity.ok().body(new CommonResponseDto<>(followerList));
    }

    // 사용자 팔로잉 목록 조회
    @ApiOperation(value = "사용자 팔로잉 목록 조회")
    @GetMapping("/following")
    public ResponseEntity<? extends ResponseDto> getFollowingsByFollowingId() {
        User user = userService.getMyInfo();
        List<User> followerList = followService.getFollowingsByFollowerId(user.getId());
        return ResponseEntity.ok().body(new CommonResponseDto<>(followerList));
    }

    // 사용자 팔로잉 추가
    @ApiOperation(value = "사용자 팔로잉 추가")
    @PostMapping("/{followingId}")
    public ResponseEntity createFollowing(@PathVariable("followingId") Long followingId) {
        User user = userService.getMyInfo();
        followService.createFollowing(user, followingId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 사용자 팔로워 삭제
    @ApiOperation(value = "사용자 팔로워 삭제")
    @DeleteMapping("/follower/{followerId}")
    public ResponseEntity deleteFollower(@PathVariable("followerId") Long followerId) {
        User user = userService.getMyInfo();
        followService.deleteFollow(user.getId(), followerId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 사용자 팔로잉 삭제
    @ApiOperation(value = "사용자 팔로잉 삭제")
    @DeleteMapping("/following/{followingId}")
    public ResponseEntity deleteFollowing(@PathVariable("followingId") Long followingId) {
        User user = userService.getMyInfo();
        followService.deleteFollow(followingId, user.getId());

        return new ResponseEntity(HttpStatus.OK);
    }

    // 사용자 팔로잉 여부 조회
    @ApiOperation(value = "사용자 팔로잉 여부 조회")
    @GetMapping("/check/{followingId}")
    public ResponseEntity<Boolean> checkUserFollowing(@PathVariable("followingId") Long followingId) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(followService.checkUserFollowing(user.getId(), followingId));
    }
}
