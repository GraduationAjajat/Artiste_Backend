package com.graduationajajat.artiste.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 작품 응답
public class ArtResponseDto {

    private Long artId;

    private String artImage;

    private String artName;

    private String artDesc;


}