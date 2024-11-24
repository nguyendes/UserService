package com.example.UserService.sys.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String email;

}
