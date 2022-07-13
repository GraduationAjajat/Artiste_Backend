package com.graduationajajat.artiste.dto;

import com.graduationajajat.artiste.model.Art;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDetailResponseDto {

    private Long exhibitionId;

    private String exhibitionName;

    private String username;

    private String exhibitionDesc;

    private List<Art> artList;
}
