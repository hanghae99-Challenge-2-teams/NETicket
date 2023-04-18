//package com.example.neticket.event.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import com.example.neticket.event.dto.EventRequestDto;
//import com.example.neticket.event.entity.Event;
//import com.example.neticket.event.entity.TicketInfo;
//import com.example.neticket.event.repository.EventRepository;
//import com.example.neticket.event.repository.TicketInfoRepository;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//class EventServiceTest {
//
//  @Autowired
//  private EventService eventService;
//
//  @Autowired
//  private EventRepository eventRepository;
//
//  @Autowired
//  private TicketInfoRepository ticketInfoRepository;
//
//  @Test
//  @DisplayName("메인페이지 조회")
//  void getEventList() {
//    // given (이런게 주어지고)
//    List<EventRequestDto> eventRequestDtoList = new ArrayList<>();
//    eventRequestDtoList.add(
//        new EventRequestDto("title1", "place1", 30000, LocalDateTime.parse("2023-04-01T00:00:00"),
//            LocalDateTime.parse("2023-03-01T00:00:00"), 50000));
//    eventRequestDtoList.add(
//        new EventRequestDto("title2", "place2", 30000, LocalDateTime.parse("2023-04-01T00:00:00"),
//            LocalDateTime.parse("2023-03-01T00:00:00"), 50000));
//    eventRequestDtoList.add(
//        new EventRequestDto("title3", "place3", 30000, LocalDateTime.parse("2023-04-01T00:00:00"),
//            LocalDateTime.parse("2023-03-01T00:00:00"), 50000));
//    eventRequestDtoList.add(
//        new EventRequestDto("title4", "place4", 30000, LocalDateTime.parse("2023-04-01T00:00:00"),
//            LocalDateTime.parse("2023-03-01T00:00:00"), 50000));
//    eventRequestDtoList.add(
//        new EventRequestDto("title5", "place5", 30000, LocalDateTime.parse("2023-04-01T00:00:00"),
//            LocalDateTime.parse("2023-03-01T00:00:00"), 50000));
//    eventRequestDtoList.add(
//        new EventRequestDto("title6", "place6", 30000, LocalDateTime.parse("2023-04-01T00:00:00"),
//            LocalDateTime.parse("2023-03-01T00:00:00"), 50000));
//
//    // when (이렇게 했을때)
//
//    for (EventRequestDto dto : eventRequestDtoList) {
//      Event event = eventRepository.save(new Event(dto, "image1"));
//      ticketInfoRepository.save(new TicketInfo(dto, event));
//    }
//
//
//    // than (이렇게 된다)
//
//    // 기본값이 예매 불가능이기 때문에 조회X 확인하려면 ticketinfo entity에 isAvailable = true 로 해야함
//    Pageable pageable = PageRequest.of(0, 4);
//    Page<Event> events = eventRepository.findAllByAvailableOrderByticketInfoDate(pageable);
//
//    assertEquals(6, events.getTotalElements());
//    assertEquals(4, events.getContent().size());
//    assertThat(events.getContent().get(0).getTitle()).isEqualTo("title1");
//    assertThat(events.getContent().get(0).getPlace()).isEqualTo("place1");
//    assertThat(events.getContent().get(0).getPrice()).isEqualTo(30000);
//    assertThat(events.getContent().get(0).getDate()).isEqualTo(
//        LocalDateTime.parse("2023-04-01T00:00:00"));
//
//  }
//
//  @Test
//  @DisplayName("상세페이지 조회")
//  void getEvent() {
//    // given (이런게 주어지고)
//    EventRequestDto eventRequestDto = new EventRequestDto("title1", "place1", 30000,
//        LocalDateTime.parse("2023-04-01T00:00:00"), LocalDateTime.parse("2023-03-01T00:00:00"),
//        50000);
//
//    // when (이렇게 했을때)
//
//    Event event = eventRepository.save(new Event(eventRequestDto, "image1"));
//    ticketInfoRepository.save(new TicketInfo(eventRequestDto, event));
//
//    // than (이렇게 된다)
//
//    Event getEvent = eventRepository.findById(7L).orElseThrow();
//    assertThat(getEvent.getId()).isEqualTo(7L);
//    assertThat(getEvent.getPlace()).isEqualTo("place1");
//    assertThat(getEvent.getPrice()).isEqualTo(30000);
//    assertThat(getEvent.getDate()).isEqualTo(LocalDateTime.parse("2023-04-01T00:00:00"));
//    assertThat(getEvent.getTicketInfo().getId()).isEqualTo(7L);
//    assertThat(getEvent.getTicketInfo().getOpenDate()).isEqualTo(
//        LocalDateTime.parse("2023-03-01T00:00:00"));
//    assertThat(getEvent.getTicketInfo().getLeftSeats()).isEqualTo(50000);
//
//  }
//
//}