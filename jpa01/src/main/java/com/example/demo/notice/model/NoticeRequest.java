package com.example.demo.notice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class NoticeRequest {
    private String title;
    private String contents;
}
