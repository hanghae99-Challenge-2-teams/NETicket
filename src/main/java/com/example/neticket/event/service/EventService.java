package com.example.neticket.event.service;

import com.example.neticket.event.dto.EventResponseDto;
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


  @Transactional(readOnly = true)
  public Page<EventResponseDto> getEvents(int page) {
    Sort.Direction direction = Sort.Direction.ASC;
    Sort sort = Sort.by(direction, "date");
    Pageable pageable = PageRequest.of(page, 8, sort);
//    여기서 어떤 repository를 써서 가져와야 할까?



  }
}
