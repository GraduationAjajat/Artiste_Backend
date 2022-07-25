package com.graduationajajat.artiste.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 팔로워/팔로잉 수
public class CountFollowResponseDto {

    private int followerCount;

    private int followingCount;
}
