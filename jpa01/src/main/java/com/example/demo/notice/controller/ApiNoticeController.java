package com.example.demo.notice.controller;

import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.exception.AlreadyDeletedException;
import com.example.demo.notice.exception.NoticeNotFoundException;
import com.example.demo.notice.model.NoticeDeleteRequest;
import com.example.demo.notice.model.NoticeRequest;
import com.example.demo.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/notice")
public class ApiNoticeController {

    private final NoticeRepository noticeRepository; // 생성자 주입

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable("id") long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다. id: " + id));

        return ResponseEntity.status(HttpStatus.OK).body(notice);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Notice> addNotice(@RequestBody @Valid NoticeRequest noticeRequest) {
        String title = noticeRequest.getTitle();
        String contents = noticeRequest.getContents();

        Notice notice = Notice.builder()
                .title(title)
                .contents(contents)
                .hits(0)
                .likes(0)
                .registeredAt(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noticeRepository.save(notice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(
            @PathVariable("id") long id,
            @RequestBody NoticeRequest noticeRequest) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다. id: " + id));

        notice.setTitle(noticeRequest.getTitle())
                .setContents(noticeRequest.getContents())
                .setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK).body(noticeRepository.save(notice));
    }

    @PatchMapping("/{id}/hits")
    public ResponseEntity<Notice> updateNoticeHits(@PathVariable("id") long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다. id: " + id));

        notice.setHits(notice.getHits() + 1);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noticeRepository.save(notice));
    }

    @PatchMapping("/{id}/likes")
    public ResponseEntity<Notice> updateNoticeLikes(@PathVariable("id") long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다. id: " + id));

        notice.setLikes(notice.getLikes() + 1);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noticeRepository.save(notice));
    }

    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable("id") long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다. id: " + id));

        if (notice.isDeleted()) {
            throw new AlreadyDeletedException("공지사항 글이 이미 삭제되었습니다. id: " + id + ", Date: " + notice.getDeletedAt());
        }

        notice.setDeleted(true);
        notice.setDeletedAt(LocalDateTime.now());

        noticeRepository.save(notice);
    }

    @DeleteMapping({"", "/"})
    public void deleteNoticeList(@RequestBody NoticeDeleteRequest noticeDeleteRequest) {
        List<Notice> noticeList = noticeRepository
                .findByIdIn(noticeDeleteRequest.getIdList())
                .orElseThrow(() -> new NoticeNotFoundException("공지사항 글이 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        noticeList.forEach(notice -> {
            log.info("{}", notice);
            notice.setDeleted(true);
            notice.setDeletedAt(now);
        });

        noticeRepository.saveAll(noticeList);
    }

    @DeleteMapping("/all")
    public void deleteNoticeAll() {
        List<Notice> noticeList = noticeRepository
                .findByDeletedFalse()
                .orElseThrow(() -> new NoticeNotFoundException("공지사항 글이 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        noticeList.forEach(notice -> {
            log.info("{}", notice);
            notice.setDeleted(true);
            notice.setDeletedAt(now);
        });

        noticeRepository.saveAll(noticeList);
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<String> handlerNoticeNotFoundException(NoticeNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<String> handlerAlreadyDeletedException(AlreadyDeletedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }

}
