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
// 전시회 목록
public class ExhibitionResponseDto {

    private Long exhibitionId;

    private String exhibitionName;

    private String thumbnail;

    private String username;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private List<Tag> tagList;

    private int scrapCount;

}
