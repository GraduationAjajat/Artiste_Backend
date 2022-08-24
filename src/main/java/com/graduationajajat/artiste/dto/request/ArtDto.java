package com.graduationajajat.artiste.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 작품 등록
public class ArtDto {

    @NotNull
    private MultipartFile artImage;

    @NotNull
    private String artName;

    @NotNull
    private String artDesc;


}
