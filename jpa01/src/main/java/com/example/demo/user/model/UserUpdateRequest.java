package com.example.demo.user.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Size(max = 20, message = "올바르지 않은 전화번호 형식입니다.")
    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phone;
}
