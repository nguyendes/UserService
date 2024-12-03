package com.example.UserService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(404, "User Not Found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(400, "User Already Exists"),
    USER_NOT_EXISTS(400, "User Not Exists"),
    USER_ALREADY_DELETED(400, "User Already Deleted"),
    USER_ALREADY_UPDATED(400, "User Already Updated"),
    USERNAME_INVALID(400, "Username Invalid"),
    PASSWORD_INVALID(400, "Password Invalid"),
    MESSAGE_INVALID(400, "Message Invalid"),
    UNCATEGORIZED_ERROR(500, "Uncategorized Error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED_ERROR(401, "Unauthenticated Error", HttpStatus.UNAUTHORIZED),
    SIGNATURE_INVALID(404, "Signature Invalid"),
    EXPIRED_TOKEN(401, "Expired Token"),
    FORBIDDEN_TOKEN(403, "Forbidden Token"),
    UNAUTHORIZED_ERROR(401, "Unauthorized User", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    // Constructor mặc định nếu không truyền HttpStatus
    ErrorCode(int code, String message) {
        this(code, message, HttpStatus.BAD_REQUEST);
    }
}
