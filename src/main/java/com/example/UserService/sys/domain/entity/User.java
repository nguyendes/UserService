package com.example.UserService.sys.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username",unique = true, nullable = false)
    String username;

    String password;
    String firstName;
    String lastName;
    LocalDate birthDate;
    String email;

    @ElementCollection(fetch = FetchType.EAGER)
    Set<String> roles;
}
