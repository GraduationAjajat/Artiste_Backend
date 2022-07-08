package com.graduationajajat.artiste.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduationajajat.artiste.model.Authority;
import com.graduationajajat.artiste.model.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    private LocalDate birthday;

    @NotNull
    private int gender;

    private String profileImage;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .birthday(birthday)
                .gender(gender)
                .profileImage(profileImage)
                .authority(Authority.ROLE_USER)
                .build();
    }

}