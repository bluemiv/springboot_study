package com.example.demo.notice.controller;

import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.exception.AlreadyDeletedException;
import com.example.demo.notice.exception.DuplicateNoticeException;
import com.example.demo.notice.exception.NoticeNotFoundException;
import com.example.demo.notice.model.ErrorResponse;
import com.example.demo.notice.model.NoticeDeleteRequest;
import com.example.demo.notice.model.NoticeRequest;
import com.example.demo.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/notice")
public class ApiNoticeController {

    private final NoticeRepository noticeRepository; // 생성자 주입

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable("id") long id) {
        // id 를 통해서 Notice 글을 가지고 옴
        // 만약, 없다면 NoticeNotFoundException 발생
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다. id: " + id));

        return ResponseEntity.status(HttpStatus.OK).body(notice);
    }

    @GetMapping("/latest/{size}")
    public ResponseEntity<Page<Notice>> noticeLatest(@PathVariable("size") int size) {

        // 최근 항목 순으로 Size 만큼 Notice 글을 가지고 옴
        Page<Notice> noticePage = noticeRepository.findAll(
                PageRequest.of(0, size, Sort.Direction.DESC, "registeredAt"));

        return ResponseEntity.status(HttpStatus.OK).body(noticePage);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Object> addNotice(
            @RequestBody @Valid NoticeRequest noticeRequest,
            Errors errors) {

        // body 데이터에 오류가 있는 경우, field 랑 message 반환
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = new ArrayList<>();

            errors.getAllErrors().forEach(error -> {
                errorResponses.add(ErrorResponse.of(error));
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponses);
        }

        String title = noticeRequest.getTitle();
        String contents = noticeRequest.getContents();

        // 1분 안에 동일한 Request 가 오는 경우,
        // 중복 글로 취급하여 등록이 안되도록 DuplicationNoticeException 을 발생시킴
        LocalDateTime checkDate = LocalDateTime.now().minusMinutes(1L);
        if (noticeRepository.countByTitleAndContentsAndRegisteredAtGreaterThanEqual(title, contents, checkDate) > 0) {
            throw new DuplicateNoticeException("이미 등록된 글입니다.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noticeRepository.save(
                        Notice.builder()
                                .title(title)
                                .contents(contents)
                                .hits(0)
                                .likes(0)
                                .registeredAt(LocalDateTime.now())
                                .build()));
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

        notice.setDeleted(true)
                .setDeletedAt(LocalDateTime.now());

        // 실제로 삭제 하지 않고, deleted 플래그 값을 true 로 변경
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
            notice.setDeleted(true)
                    .setDeletedAt(now);
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
            notice.setDeleted(true)
                    .setDeletedAt(now);
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

    @ExceptionHandler(DuplicateNoticeException.class)
    public ResponseEntity<String> handlerDuplicateNoticeException(DuplicateNoticeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
