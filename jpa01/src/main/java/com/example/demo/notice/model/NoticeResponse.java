package com.example.demo.notice.model;

import com.example.demo.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NoticeResponse {
    private long id;
    private String title;
    private String contents;
    private long hits;
    private long likes;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private long userId;
    private String username;

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .hits(notice.getHits())
                .likes(notice.getLikes())
                .registeredAt(notice.getRegisteredAt())
                .updatedAt(notice.getUpdatedAt())
                .userId(notice.getUser().getId())
                .username(notice.getUser().getUsername())
                .build();
    }
}
