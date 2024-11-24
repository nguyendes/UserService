package com.example.UserService.sys.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationRequest {

    @Size(min = 3, max = 20, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String email;

}
