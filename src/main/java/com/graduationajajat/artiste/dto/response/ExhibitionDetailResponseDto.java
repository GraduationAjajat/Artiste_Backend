package com.graduationajajat.artiste.dto.response;

import com.graduationajajat.artiste.model.ExhibitionTagName;
import com.graduationajajat.artiste.model.User;
import lombok.*;

import java.time.LocalDateTime;
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

    private User exhibitionArtist;

    private String exhibitionDesc;

    private LocalDateTime exhibitionStartDate;

    private LocalDateTime exhibitionEndDate;

    private List<ExhibitionTagName> tagList;

    private List<ArtResponseDto> artList;
}
