package com.example.UserService.sys.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class IntrospectResponse {
    boolean valid;
}
