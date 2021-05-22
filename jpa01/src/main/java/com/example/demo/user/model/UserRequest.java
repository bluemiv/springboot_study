package com.example.demo.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Size(min = 4, max = 15, message = "username 은 4자 이상 15자 이하입니다.")
    @NotBlank(message = "username 은 필수 항목입니다.")
    private String username;

    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하입니다.")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @Email
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @Size(max = 20, message = "올바르지 않은 전화번호 형식입니다.")
    @NotBlank(message = "전화번호는 필수 항목입니다.")
    private String phone;
}
