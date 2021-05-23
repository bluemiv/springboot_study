package com.example.demo.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UserPasswordUpdateRequest {

    @NotBlank(message = "현재 비밀번호는 필수 항목입니다.")
    private String password;

    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하입니다.")
    @NotBlank(message = "신규 비밀번호는 필수 항목입니다.")
    private String newPassword;

}
