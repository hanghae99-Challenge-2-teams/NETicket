package com.example.neticket.event.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventRequestDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.service.EventService;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  //  메인 페이지 조회
  @GetMapping
  public ResponseEntity<Page<EventResponseDto>> getEvents(@RequestParam(value = "page") int page) {
    Page<EventResponseDto> events = eventService.getEvents(page - 1);
    return ResponseEntity.ok().body(events);

  }

  //  상세 페이지 조회
  @GetMapping("/{eventId}")
  public ResponseEntity<DetailEventResponseDto> getDetailEvent(@PathVariable Long eventId) {
    DetailEventResponseDto detailEvent = eventService.getDetailEvent(eventId);
    return ResponseEntity.ok().body(detailEvent);

  }

  // 공연 추가하기
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public MessageResponseDto addEvent(
      @RequestParam("image")MultipartFile image,
      @RequestPart("dto")EventRequestDto eventRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    validateImage(image);
    return eventService.addEvent(eventRequestDto, userDetails.getUser(), image);
  }

//  //  공연 삭제 Reservation 기록이 있으면 삭제가 안되기때문에 추후 논의 반드시 필요!!!!!!!!!!!
//  @DeleteMapping("/{eventId}")
//  public ResponseEntity<MessageResponseDto> deleteEvent(@PathVariable Long eventId,
//      @AuthenticationPrincipal UserDetailsImpl userDetails) {
//    MessageResponseDto deleteMessage = eventService.deleteEvent(eventId, userDetails.getUser());
//    return ResponseEntity.ok().body(deleteMessage);
//
//  }

  /**
   * 검색기능 keyword로 공연 제목과 공연 장소를 검색 저가순은 sordBy에 price, isAsc에 true를 담아 보내주면 된다. 고가순은 sortBy에 price,
   * isAsc에 false를 담아 보내주면 된다. 나머지 정렬 방식은 추후 논의 후 결정 (정확도순이나 카테고리 기능 날짜검색 등)
   *
   * @param keyword : 검색어
   * @param page    : 현재 페이지. Pageable에서는 0페이지가 첫페이지라 -1
   * @param sortBy  : 정렬 기준
   * @param isAsc   : 오름차순이면 true, 내림차순이면 false
   * @return : 검색결과를 Page<EventResponseDto>로 반환
   */
  // 쿼리스트링 값 예외처리 추후에 해야함
  @GetMapping("/search")
  public ResponseEntity<Page<EventResponseDto>> searchEvents(
      @RequestParam(value = "keyword") String keyword,
      @RequestParam int page, @RequestParam String sortBy, @RequestParam boolean isAsc) {
    if (keyword.isBlank()) {
      throw new CustomException(ExceptionType.ZERO_WORD_EXCEPTION);
    }
    Page<EventResponseDto> searchEvents = eventService.searchEvents(keyword, page - 1, sortBy,
        isAsc);
    return ResponseEntity.ok().body(searchEvents);

  }

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

  private void validateSearch(String keyword, int page, String sortBy, boolean isAsc) {
//    쿼리스트링으로 받을꺼면 추후 작성해야. dto로 받을꺼면 @Valid로 가능
  }

}
