package com.example.neticket.event.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.repository.EventRepository;
import com.example.neticket.event.repository.ShowTimeRepository;
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
  private final ShowTimeRepository showTimeRepository;


//  메인페이지 조회
  @Transactional(readOnly = true)
  public Page<EventResponseDto> getEvents(int page) {
//    ShowTime의 isAvailable이 true인 Event를 ShowTime의 date가 가장 빠른 순서대로 정렬하여 Page<EventResponseDto>로 반환
    Sort.Direction direction = Sort.Direction.ASC;
    Sort sort = Sort.by(direction, "showTimeList.date");
    Pageable pageable = PageRequest.of(page, 8, sort);
    return eventRepository.findAllByAvailableOrderByShowTimeDate(pageable)
        .map(EventResponseDto::new);

  }

//  상세 페이지 조회
  @Transactional(readOnly = true)
  public DetailEventResponseDto getDetailEvent(Long eventId) {
    Event event = eventRepository.findById(eventId).orElseThrow(
        () -> new IllegalArgumentException("조회하려는 공연 정보가 없습니다.")
    );
    return new DetailEventResponseDto(event);

  }
}
