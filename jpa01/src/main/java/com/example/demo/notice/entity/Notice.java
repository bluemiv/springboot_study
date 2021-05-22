package com.example.demo.notice.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // PK

    @Size(min = 10, max = 100, message = "제목은 10자 이상 100자 이하 입니다.")
    @NotBlank(message = "제목은 필수 항목입니다.")
    @Column
    private String title; // 제목

    @Size(min = 10, max = 1500, message = "내용은 10자 이상 1500자 이하 입니다.")
    @NotBlank(message = "내용은 필수 항목입니다.")
    @Column
    private String contents; // 내용

    @Column
    private long hits; // 조회수

    @Column
    private long likes; // 좋아요 수

    @Column
    private boolean deleted; // 삭제 여부

    @Column
    private LocalDateTime registeredAt; // 등록 날짜

    @Column
    private LocalDateTime updatedAt; // 수정 날짜

    @Column
    private LocalDateTime deletedAt; // 삭제 날짜
}
