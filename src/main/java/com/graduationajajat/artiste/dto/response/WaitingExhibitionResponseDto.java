package com.graduationajajat.artiste.dto.response;

import com.graduationajajat.artiste.model.ExhibitionState;
import com.graduationajajat.artiste.model.ExhibitionTagName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 대기 중인 전시회 목록
public class WaitingExhibitionResponseDto {
    private Long exhibitionId;

    private String exhibitionName;

    private String exhibitionThumbnail;

    private String exhibitionArtistName;

    private LocalDateTime exhibitionStartDate;

    private LocalDateTime exhibitionEndDate;

    private List<ExhibitionTagName> tagList;

    private ExhibitionState exhibitionState;
}
