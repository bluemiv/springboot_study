package com.example.demo.notice.model;

import lombok.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String field;
    private String message;

    public static ErrorResponse of(ObjectError error) {
        return ErrorResponse.builder()
                .field(((FieldError) error).getField())
                .message(error.getDefaultMessage())
                .build();
    }
}
