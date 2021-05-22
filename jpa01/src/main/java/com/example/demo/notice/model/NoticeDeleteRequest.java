package com.example.demo.notice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeDeleteRequest {
    private List<Long> idList;
}
