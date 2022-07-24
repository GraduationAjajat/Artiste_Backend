package com.graduationajajat.artiste.dto;

import com.graduationajajat.artiste.model.Tag;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 작품 등록
public class ArtDto {

    @NotNull
    private String artImage;

    @NotNull
    private String artName;

    @NotNull
    private String artDesc;


}
