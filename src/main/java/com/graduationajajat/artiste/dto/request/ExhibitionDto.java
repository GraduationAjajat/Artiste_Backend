package com.graduationajajat.artiste.dto.request;

import com.graduationajajat.artiste.dto.request.ArtDto;
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
// 전시회 등록
public class ExhibitionDto {

    @NotNull
    private String exhibitionName;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private String exhibitionDesc;

    @NotNull
    private List<Tag> tagList;

    @NotNull
    private List<ArtDto> artList;

}

