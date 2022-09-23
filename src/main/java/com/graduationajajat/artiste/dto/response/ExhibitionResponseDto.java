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
// 전시회 목록
public class ExhibitionResponseDto {

    private Long exhibitionId;

    private String exhibitionName;

    private String exhibitionThumbnail;

    private String exhibitionArtistName;

    private LocalDateTime exhibitionStartDate;

    private LocalDateTime exhibitionEndDate;

    private List<ExhibitionTagName> tagList;

    private ExhibitionState exhibitionState;

    private boolean scrapFlag;

    private int scrapCount;

    private int commentCount;

}
