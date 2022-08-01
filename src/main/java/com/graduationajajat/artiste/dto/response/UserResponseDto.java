package com.graduationajajat.artiste.dto.response;

import com.graduationajajat.artiste.model.Comment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 관리자 - 회원 관리
public class UserResponseDto {
    private Long userId;

    private String nickname;

    private String profileImage;

    private LocalDateTime registerDate;

    private Long exhibitionCount;

    private Long commentCount;

}
