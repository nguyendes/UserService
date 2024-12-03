package com.example.UserService.sys.service;

import com.example.UserService.common.enums.Role;
import com.example.UserService.exception.AppException;
import com.example.UserService.exception.ErrorCode;
import com.example.UserService.sys.domain.dto.request.UserCreationRequest;
import com.example.UserService.sys.domain.dto.request.UserUpdateRequest;
import com.example.UserService.sys.domain.entity.User;
import com.example.UserService.sys.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserMapper userMapper
//    User user = UserMapper.toUser(request);

    public User createUser(UserCreationRequest request) {

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
//            throw new RuntimeException("Username already exists");
//            throw new RuntimeException("ErrorCode.UNCATEGORIZED_ERROR");

//        su dung builder
//        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
//                .username(request.getUsername())
//                .password(request.getPassword())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .build();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setEmail(request.getEmail());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);


        return userRepository.save(user);
    }

    public User getUserInfo() {
        var context= SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user= userRepository.findByUsername(username).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTS)
        );
        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        log.info("getAllUsers");
        return userRepository.findAll();
    }

    @PostAuthorize("hasRole('ADMIN')")
    public User getUserById(String id) {
        log.info("by admin");
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUserById(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthDate(request.getBirthDate());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }
}
