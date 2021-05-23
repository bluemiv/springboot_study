package com.example.demo.user.controller;

import com.example.demo.notice.model.ErrorResponse;
import com.example.demo.notice.model.NoticeResponse;
import com.example.demo.notice.repository.NoticeRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.exception.UserNotFoundException;
import com.example.demo.user.model.UserRequest;
import com.example.demo.user.model.UserResponse;
import com.example.demo.user.model.UserUpdateRequest;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class ApiUserController {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    private ResponseEntity<List<ErrorResponse>> getErrorResponseEntity(Errors errors) {
        // error 정보 response 로 반환
        List<ErrorResponse> errorResponses = new ArrayList<>();
        errors.getAllErrors().forEach(error -> {
            errorResponses.add(ErrorResponse.of(error));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자는 존재하지 않습니다. id: " + id));

        // 비밀번호, 가입일과 같이 민감 정보는 숨기기 위해, 따로 UserResponse 를 생성하여 반환
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.of(user));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> addUser(@RequestBody @Valid UserRequest userRequest, Errors errors) {

        // request 데이터 검사
        if (errors.hasErrors()) {
            return getErrorResponseEntity(errors);
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


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest,
            Errors errors) {

        // request 데이터 검사
        if (errors.hasErrors()) {
            return getErrorResponseEntity(errors);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자는 존재하지 않습니다. id: " + id));

        user.setPhone(userUpdateRequest.getPhone())
                .setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
    }

    @GetMapping("/{id}/notice")
    public ResponseEntity<?> userNotice(@PathVariable("id") long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자는 존재하지 않습니다. id: " + id));

        List<NoticeResponse> noticeResponseList =
                noticeRepository.findByUser(user)
                        .stream().map(notice -> NoticeResponse.of(notice))
                        .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(noticeResponseList);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlerUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
