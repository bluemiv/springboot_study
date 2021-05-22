package com.example.demo.notice.repository;

import com.example.demo.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<List<Notice>> findByDeletedFalse();

    Optional<List<Notice>> findByIdIn(List<Long> idList);
}
