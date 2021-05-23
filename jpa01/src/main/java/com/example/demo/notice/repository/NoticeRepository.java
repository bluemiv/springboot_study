package com.example.demo.notice.repository;

import com.example.demo.notice.entity.Notice;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<List<Notice>> findByDeletedFalse();

    Optional<List<Notice>> findByIdIn(List<Long> idList);

    long countByTitleAndContentsAndRegisteredAtGreaterThanEqual(String title, String contents, LocalDateTime registeredAt);

    List<Notice> findByUser(User user);
}
