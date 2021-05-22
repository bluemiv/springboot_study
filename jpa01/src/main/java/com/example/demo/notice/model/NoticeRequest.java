package com.example.demo.notice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
public class NoticeRequest {

    @Size(min = 10, max = 100, message = "제목은 10자 이상 100자 이하 입니다.")
    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @Size(min = 10, max = 1500, message = "내용은 10자 이상 1500자 이하 입니다.")
    @NotBlank(message = "내용은 필수 항목입니다.")
    private String contents;
}
