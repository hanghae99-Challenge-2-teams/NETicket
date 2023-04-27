package com.example.neticket.event.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventRequestDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.service.EventService;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.security.UserDetailsImpl;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/neticket/events")
public class EventController {

  private final EventService eventService;
  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
  private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = List.of("image/jpeg", "image/jpg",
      "image/png", "image/gif");

  //  1.메인 페이지 조회
  @GetMapping
  public ResponseEntity<Page<EventResponseDto>> getEvents(@RequestParam(value = "page") int page) {
    validateSearch("pass", page);
    Page<EventResponseDto> events = eventService.getEvents(page - 1);
    return ResponseEntity.ok().body(events);

  }

  //  2.상세 페이지 조회
  @GetMapping("/{eventId}")
  public ResponseEntity<DetailEventResponseDto> getDetailEvent(@PathVariable Long eventId) {
    DetailEventResponseDto detailEvent = eventService.getDetailEvent(eventId);
    return ResponseEntity.ok().body(detailEvent);

  }

  // 3.공연 추가하기
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public MessageResponseDto addEvent(
      @RequestParam("image") MultipartFile image,
      @RequestPart("dto") @Valid EventRequestDto eventRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    validateImage(image);
    return eventService.addEvent(eventRequestDto, userDetails.getUser(), image);

  }

  /**
   * 4.검색기능 keyword로 공연 제목과 공연 장소를 검색
   *
   * @param keyword : 검색어
   * @param page    : 현재 페이지. Pageable에서는 0페이지가 첫페이지라 -1
   * @return : 검색결과를 Page<EventResponseDto>로 반환
   */
  @GetMapping("/search")
  public ResponseEntity<Page<EventResponseDto>> searchEvents(
      @RequestParam(value = "keyword") String keyword,
      @RequestParam(value = "page") int page) {
    validateSearch(keyword, page);
    Page<EventResponseDto> searchEvents = eventService.searchEvents(keyword, page - 1);
    return ResponseEntity.ok().body(searchEvents);

  }

  // 이미지 유효성 검사
  private void validateImage(MultipartFile image) {
    if (image.isEmpty()) {
      throw new CustomException(ExceptionType.NO_IMAGE_EXCEPTION);
    }
    if (image.getSize() > MAX_FILE_SIZE) {
      throw new CustomException(ExceptionType.IMAGE_SIZE_EXCEPTION);
    }
    if (!ALLOWED_IMAGE_CONTENT_TYPES.contains(image.getContentType())) {
      throw new CustomException(ExceptionType.IMAGE_FORMAT_EXCEPTION);
    }

  }

  // 검색 유효성 검사
  private void validateSearch(String keyword, int page) {
    if (keyword.isBlank()) {
      throw new CustomException(ExceptionType.ZERO_WORD_EXCEPTION);
    }
    if (page <= 0) {
      throw new CustomException(ExceptionType.INVALID_PAGE_EXCEPTION);
    }

  }

}
