package com.graduationajajat.artiste.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @NotNull
    private Long exhibitionId;

    @NotNull
    private String commentContent;
}
