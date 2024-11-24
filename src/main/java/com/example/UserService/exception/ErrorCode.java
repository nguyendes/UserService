package com.example.UserService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(404, "User Not Found"),
    USER_ALREADY_EXISTS(400, "User Already Exists"),
    USER_NOT_EXISTS(400, "User Not Exists"),
    USER_ALREADY_DELETED(400, "User Already Deleted"),
    USER_ALREADY_UPDATED(400, "User Already Updated"),
    USERNAME_INVALID(400, "Username Invalid"),
    PASSWORD_INVALID(400, "Password Invalid"),
    MESSAGE_INVALID(401, "Message Invalid"),
    UNCATEGORIZED_ERROR(500, "Uncategorized Error"),
    UNAUTHENTICATED_ERROR(404, "Unauthenticated Error"),
    SIGNATURE_INVALID(404, "Signature Invalid"),
    EXPIRED_TOKEN(401, "Expired Token"),
    FORBIDDEN_TOKEN(403, "Forbidden Token"),
    ;

    private int code;
    private String message;
}
