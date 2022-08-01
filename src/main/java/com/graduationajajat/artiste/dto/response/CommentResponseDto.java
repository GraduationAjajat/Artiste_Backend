package com.graduationajajat.artiste.dto.response;

import com.graduationajajat.artiste.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 전시회 댓글용 응답
public class CommentResponseDto {

    private Long commentId;

    private User user;

    private String commentContent;

    private LocalDateTime commentDate;

}
