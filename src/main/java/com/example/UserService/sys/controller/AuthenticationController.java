package com.example.UserService.sys.controller;

import com.example.UserService.sys.domain.dto.request.AuthenticationRequest;
import com.example.UserService.sys.domain.dto.request.IntrospectRequest;
import com.example.UserService.sys.domain.dto.response.ApiResponse;
import com.example.UserService.sys.domain.dto.response.AuthenticationResponse;
import com.example.UserService.sys.domain.dto.response.IntrospectResponse;
import com.example.UserService.sys.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticateToken(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .authenticated(result.isAuthenticated())
                        .token(result.getToken())
                        .roles(result.getRoles())
                        .build())
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var result = authenticationService.introspect(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}

