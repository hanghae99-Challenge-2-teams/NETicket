package com.example.neticket.event.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

//  메인페이지 조회
  @Transactional(readOnly = true)
  public Page<EventResponseDto> getEvents(int page) {
//    ShowTime의 isAvailable이 true인 Event를 ShowTime의 date가 가장 빠른 순서대로 정렬하여 Page<EventResponseDto>로 반환
    Pageable pageable = PageRequest.of(page, 4);
    return eventRepository.findAllByAvailableOrderByShowTimeDate(pageable)
        .map(EventResponseDto::new);

  }

//  상세 페이지 조회
  @Transactional(readOnly = true)
  public DetailEventResponseDto getDetailEvent(Long eventId) {
    return eventRepository.findById(eventId)
        .map(DetailEventResponseDto::new)
        .orElseThrow(() -> new IllegalArgumentException("조회하려는 공연 정보가 없습니다."));
  }

//  검색기능
  @Transactional(readOnly = true)
  public Page<EventResponseDto> searchEvents(String keyword, int page, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, 4, sort);
    return eventRepository.findAllByTitleOrPlaceContaining(keyword, pageable)
        .map(EventResponseDto::new);

  }

}
