package com.example.demo.user.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // PK

    @Column
    private String username; // 사용자 username

    @Column
    private String password; // 비밀번호

    @Column
    private String email; // 이메일

    @Column
    private String phone; // 전화번호

    @Column
    private LocalDateTime registeredAt; // 등록일 (가입일)

    @Column
    private LocalDateTime updatedAt; // 수정일
}
