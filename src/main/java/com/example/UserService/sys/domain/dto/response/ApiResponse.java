package com.example.UserService.sys.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL) //k show cac field null
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T>{
    private int code = 1000;
    private String message;
    private T result;
}
