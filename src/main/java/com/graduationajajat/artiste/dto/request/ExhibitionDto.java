package com.graduationajajat.artiste.dto.request;

import com.graduationajajat.artiste.model.ExhibitionTagName;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 전시회 등록
public class ExhibitionDto {

    private String exhibitionName;

    private LocalDateTime exhibitionStartDate;

    private LocalDateTime exhibitionEndDate;

    private String exhibitionDesc;

    private List<ExhibitionTagName> tagList;

    private List<ArtDto> artList;

}

