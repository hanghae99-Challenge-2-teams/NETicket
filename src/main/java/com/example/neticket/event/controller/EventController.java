package com.example.neticket.event.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/neticket/events")
public class EventController {

  private final EventService eventService;

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

  /**
   * 검색기능
   * keyword로 공연 제목과 공연 장소를 검색
   * 저가순은 sordBy에 price, isAsc에 true를 담아 보내주면 된다.
   * 고가순은 sortBy에 price, isAsc에 false를 담아 보내주면 된다.
   * 나머지 정렬 방식은 추후 논의 후 결정 (정확도순이나 카테고리 기능 날짜검색 등)
   * @param keyword : 검색어
   * @param page : 현재 페이지. Pageable에서는 0페이지가 첫페이지라 -1
   * @param sortBy : 정렬 기준
   * @param isAsc : 오름차순이면 true, 내림차순이면 false
   * @return : 검색결과를 Page<EventResponseDto>로 반환
   */
  // 쿼리스트링 값 예외처리 추후에 해야함
  @GetMapping("/search")
  public ResponseEntity<Page<EventResponseDto>> searchEvents(@RequestParam(value = "keyword") String keyword,
      @RequestParam int page, @RequestParam String sortBy, @RequestParam boolean isAsc) {
    if (keyword.isBlank()) {
      throw new IllegalArgumentException("검색어를 입력해 주세요.");
    }
    Page<EventResponseDto> searchEvents = eventService.searchEvents(keyword, page - 1, sortBy,
        isAsc);
    return ResponseEntity.ok().body(searchEvents);

  }

}
