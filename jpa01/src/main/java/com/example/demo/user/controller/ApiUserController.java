package com.example.demo.user.controller;

import com.example.demo.notice.model.ErrorResponse;
import com.example.demo.user.entity.User;
import com.example.demo.user.model.UserRequest;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class ApiUserController {

    private final UserRepository userRepository;

    @PostMapping({"", "/"})
    public ResponseEntity<?> addUser(@RequestBody @Valid UserRequest userRequest, Errors errors) {

        // request 데이터 검사
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();
            errors.getAllErrors().forEach(error -> {
                errorResponses.add(ErrorResponse.of(error));
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRepository.save(
                        User.builder()
                                .username(userRequest.getUsername())
                                .password(userRequest.getPassword())
                                .email(userRequest.getEmail())
                                .phone(userRequest.getPhone())
                                .registeredAt(LocalDateTime.now())
                                .build()));
    }
}
