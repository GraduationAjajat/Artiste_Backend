package com.graduationajajat.artiste.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    private String username;

    @NotNull
    private String nickname;

    @NotNull
    private LocalDate birthday;

    @NotNull
    private int gender;

    private String profileImage;

}