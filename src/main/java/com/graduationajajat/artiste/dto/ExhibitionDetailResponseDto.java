package com.graduationajajat.artiste.dto;

import com.graduationajajat.artiste.model.Art;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 전시회 상세 정보
public class ExhibitionDetailResponseDto {

    private Long exhibitionId;

    private String exhibitionName;

    private String username;

    private String exhibitionDesc;

    private List<Art> artList;
}
