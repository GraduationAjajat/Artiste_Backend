package com.graduationajajat.artiste.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.graduationajajat.artiste.model.Authority;
import com.graduationajajat.artiste.model.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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

    private MultipartFile profileImage;

}