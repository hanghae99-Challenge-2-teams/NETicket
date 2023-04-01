package com.example.neticket.event.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
  public Page<EventResponseDto> getEvents(@RequestParam(value = "page") int page) {
    return eventService.getEvents(page);

  }

//  상세 페이지 조회
  @GetMapping("/detail/{eventId}")
  public DetailEventResponseDto getDetailEvent(@PathVariable Long eventId) {
    return eventService.getDetailEvent(eventId);
  }

}
