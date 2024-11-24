package com.example.UserService.sys.controller;


import com.example.UserService.config.SecurityConfig;
import com.example.UserService.sys.domain.dto.request.UserCreationRequest;
import com.example.UserService.sys.domain.dto.request.UserUpdateRequest;
import com.example.UserService.sys.domain.dto.response.ApiResponse;
import com.example.UserService.sys.domain.entity.User;
import com.example.UserService.sys.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    List<User> getAllUsers() {
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        log.info("user : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("grantedAuthority : {}", grantedAuthority.getAuthority()));

        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    User getUserById(@PathVariable("userId") String userId){
        return userService.getUserById(userId);
    }


    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @PutMapping("{userId}")
    User updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUserById(userId, request);
    }

    @DeleteMapping("{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUserById(userId);
        return "User has been deleted";
    }

}
