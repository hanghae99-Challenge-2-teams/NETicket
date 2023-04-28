package com.example.neticket.reservation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventRequestDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.EventRepository;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.repository.RedisRepository;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional // 테스트 끝나면 롤백
class ReservationServiceTest {

  //  Test 주체
  @Autowired
  @InjectMocks
  ReservationService reservationService;

  @Autowired
  ReservationRepository reservationRepository;

  @Autowired
  TicketInfoRepository ticketInfoRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EventRepository eventRepository;

  @Mock
  RedisRepository redisRepository;

  @Mock
  RedisTemplate<String, DetailEventResponseDto> redisTemplate;

  private static User testUser;
  private static Event testEvent;
  private static TicketInfo testTicketInfo;

  @BeforeEach
  void setUp() {
    SignupRequestDto signupRequestDto = new SignupRequestDto("unittest@test.com", "asdf1234@",
        "테스트닉네임");
    LocalDateTime eventDate = LocalDateTime.of(2023, 6, 1, 18, 0);
    LocalDateTime openDate = LocalDateTime.of(2023, 5, 1, 18, 0);
    EventRequestDto eventRequestDto = new EventRequestDto("테스트공연", "테스트장소", 99000, eventDate,
        openDate, 50000);

//    test용 user event ticketInfo 생성
    testUser = new User(signupRequestDto, "asdf1234@");
    testEvent = new Event(eventRequestDto, "testimg");
    testTicketInfo = new TicketInfo(eventRequestDto, testEvent);
    testTicketInfo.setAvailable(true);

    userRepository.save(testUser);
    eventRepository.save(testEvent);
    ticketInfoRepository.save(testTicketInfo);

  }


  @Test
  @DisplayName("DB로 공연 조회 테스트")
  void test1() {
//    given
//    Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
//    Mockito.when(ticketInfoRepository.findById(1L)).thenReturn(Optional.of(testTicketInfo));
    Long eventId = testEvent.getId();

//    when
    DetailEventResponseDto detailEventResponseDto = reservationService.verifyReservation(eventId);

//    then
    assertEquals(eventId, detailEventResponseDto.getEventId());
    assertEquals(99000, detailEventResponseDto.getPrice());
    assertEquals("테스트공연", detailEventResponseDto.getTitle());
    assertEquals("테스트장소", detailEventResponseDto.getPlace());
    assertEquals("testimg", detailEventResponseDto.getImage());
    assertEquals(LocalDateTime.of(2023, 6, 1, 18, 0), detailEventResponseDto.getDate());
    assertEquals(LocalDateTime.of(2023, 5, 1, 18, 0),
        detailEventResponseDto.getTicketInfoDto().getOpenDate());

  }

  @Test
  @DisplayName("DB로 예매하기 테스트")
  void test2() {
//    given
    int testSize = 100;
    Long ticketInfoId = testTicketInfo.getId();
    int beforeTestLeftSeats = testTicketInfo.getLeftSeats();
    int count = 4;
    ReservationRequestDto reservationRequestDto = new ReservationRequestDto(ticketInfoId, count);
//    when
    for (int i = 0; i < testSize; i++) {
      reservationService.makeReservation(reservationRequestDto, testUser);
    }
//    then
    assertEquals(testTicketInfo.getTotalSeats() - testSize * count, testTicketInfo.getLeftSeats());
  }



}