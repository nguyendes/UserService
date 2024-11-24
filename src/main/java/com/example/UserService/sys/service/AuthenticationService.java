package com.example.UserService.sys.service;


import com.example.UserService.exception.AppException;
import com.example.UserService.exception.ErrorCode;
import com.example.UserService.sys.domain.dto.request.AuthenticationRequest;
import com.example.UserService.sys.domain.dto.request.IntrospectRequest;
import com.example.UserService.sys.domain.dto.response.AuthenticationResponse;
import com.example.UserService.sys.domain.dto.response.IntrospectResponse;
import com.example.UserService.sys.domain.entity.User;
import com.example.UserService.sys.repo.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    @NonFinal
    @Value(value = "${jwt.signerKey}")
    protected String SECRET_KEY;

    @NonFinal
    @Value(value = "${jwt.valid-duration}")
    protected long VALID_DURATION;


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED_ERROR);
        }


        var token= generateToken(user);
        return AuthenticationResponse.builder().token(token)
                .authenticated(true)
                .roles(user.getRoles())
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header= new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet= new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("clownnguyen")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage()+"can't sign");
            throw new RuntimeException(e);
        }
    }


    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token= introspectRequest.getToken();
        JWSVerifier verifier= new MACVerifier(SECRET_KEY.getBytes());
        SignedJWT signedJWT= SignedJWT.parse(token);
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified & expiredTime.after(new Date()))
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }

}
