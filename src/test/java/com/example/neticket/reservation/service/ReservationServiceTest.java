package com.example.neticket.reservation.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.EventRepository;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.RedisRepository;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.entity.User;
import io.lettuce.core.RedisException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

  //  Test 주체
  @InjectMocks
  ReservationService reservationService;

  @Mock
  ReservationRepository reservationRepository;
  @Mock
  TicketInfoRepository ticketInfoRepository;
  @Mock
  EventRepository eventRepository;
  @Mock
  RedisRepository redisRepository;
  @Mock
  RedisTemplate<String, DetailEventResponseDto> redisTemplate;
  @Mock
  ValueOperations<String, DetailEventResponseDto> valueOperations;

  @Mock
  User testUser;
  @Mock
  Event testEvent;
  @Mock
  TicketInfo testTicketInfo;
  @Mock
  Reservation testReservation;

  @Nested
  @DisplayName("예매중 확인하기 로직 테스트")
  class testVerifyReservation {
    Long testEventId = 123456L;
    Long testTicketInfoId = 123456L;
    String testTitle = "테스트공연 <123>";
    String cacheKey = "DetailEventResponseDto::" + testEventId;

    @Test
    @DisplayName("DB로 공연 조회 테스트")
    void test1() {
//    given
      when(testEvent.getId()).thenReturn(testEventId);
      when(testEvent.getTitle()).thenReturn(testTitle);
      when(testEvent.getTicketInfo()).thenReturn(testTicketInfo);
      when(testTicketInfo.getId()).thenReturn(testTicketInfoId);
      when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));
//    when
      DetailEventResponseDto result = reservationService.verifyReservation(testEventId);
//    then
      assertEquals(testEventId, result.getEventId());
      assertEquals(testTitle, result.getTitle());
      assertEquals(testTicketInfoId, result.getTicketInfoDto().getTicketInfoId());
      verify(eventRepository, times(1)).findById(testEventId);
    }

    @Test
    @DisplayName("Redis로 공연 조회 테스트")
    void test2(){
//    given
      DetailEventResponseDto mockDto = mock(DetailEventResponseDto.class);
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      when(valueOperations.get(cacheKey)).thenReturn(mockDto);
      when(mockDto.getEventId()).thenReturn(testEventId);
//    when
      DetailEventResponseDto result = reservationService.verifyReservation(testEventId);
//    then
      assertEquals(testEventId, result.getEventId());
      verify(eventRepository, never()).findById(testEventId);
    }

  }

  @Nested
  @DisplayName("예매하기 로직 테스트")
  class testMakeReservations {
    Long testTicketInfoId = 123456L;
    Long testReservationId = 999999L;
    ReservationRequestDto mockDto = mock(ReservationRequestDto.class);
    int count = 1;
    int initialValue = 100;
    AtomicInteger leftSeats = new AtomicInteger(initialValue);


    @Test
    @DisplayName("DB로 예매하기 테스트")
    void test3() {
//    given
      when(mockDto.getCount()).thenReturn(count);
      when(mockDto.getTicketInfoId()).thenReturn(testTicketInfoId);
      when(redisRepository.decrementLeftSeatInRedis(testTicketInfoId, count)).thenThrow(
          RedisException.class);

      when(reservationRepository.save(any(Reservation.class))).thenAnswer(
          invocation -> testReservation);
      when(testReservation.getId()).thenReturn(testReservationId);
      when(testTicketInfo.getLeftSeats()).thenAnswer(invocation -> leftSeats.get());
      when(ticketInfoRepository.decrementLeftSeats(eq(testTicketInfoId), anyInt())).thenAnswer(invocation -> {
        int invokedCount = invocation.getArgument(1, Integer.class);
        leftSeats.addAndGet(-invokedCount);
        return count;
      });
//    when
      Long result = reservationService.makeReservation(mockDto, testUser);
//    then
      assertEquals(testReservationId, result);
      assertEquals(initialValue-count, testTicketInfo.getLeftSeats());
      verify(reservationRepository, times(1)).save(any(Reservation.class));
      verify(ticketInfoRepository, times(1)).decrementLeftSeats(testTicketInfoId, count);
      verify(redisRepository, times(1)).decrementLeftSeatInRedis(any(Long.class),any(int.class));

    }
    @Test
    @DisplayName("Redis로 예매하기 테스트")
    void test4() {
//    given
      when(mockDto.getCount()).thenReturn(count);
      when(mockDto.getTicketInfoId()).thenReturn(testTicketInfoId);
      when(reservationRepository.save(any(Reservation.class))).thenAnswer(
          invocation -> testReservation);
      when(testReservation.getId()).thenReturn(testReservationId);
      when(testTicketInfo.getLeftSeats()).thenAnswer(invocation -> leftSeats.get());
      when(redisRepository.decrementLeftSeatInRedis(eq(testTicketInfoId), anyInt())).thenAnswer(invocation -> {
        int invokedCount = invocation.getArgument(1, Integer.class);
        leftSeats.addAndGet(-invokedCount);
        return true;
      });
//    when
      Long result = reservationService.makeReservation(mockDto, testUser);
//    then
      assertEquals(testReservationId, result);
      assertEquals(initialValue-count, testTicketInfo.getLeftSeats());
      verify(redisRepository, times(1)).decrementLeftSeatInRedis(any(Long.class),any(int.class));
      verify(reservationRepository, times(1)).save(any(Reservation.class));
      verify(ticketInfoRepository, never()).decrementLeftSeats(testTicketInfoId,count);
    }

  }

  @Nested
  @DisplayName("예매완료 로직 테스트")
  class testReservationComplete{
    Long testReservationId = 999999L;
    Long testTicketInfoId = 123456L;
    Long testUserId = 111111L;
    String testTitle = "테스트공연 <123>";

    @Test
    @DisplayName("예매 완료")
    void test5() {
//    given
      when(reservationRepository.findById(testReservationId)).thenReturn(Optional.of(testReservation));
      when(testReservation.getUser()).thenReturn(testUser);
      when(testReservation.getTicketInfoId()).thenReturn(testTicketInfoId);
      when(testUser.getId()).thenReturn(testUserId);
      when(ticketInfoRepository.findById(testTicketInfoId)).thenReturn(Optional.of(testTicketInfo));
      when(testReservation.getId()).thenReturn(testReservationId);
      when(testTicketInfo.getEvent()).thenReturn(testEvent);
      when(testEvent.getTitle()).thenReturn(testTitle);
//    when
      ReservationResponseDto result = reservationService.reservationComplete(
          testReservationId, testUser);
//    then
      assertEquals(testReservationId, result.getId());
      assertEquals(testTitle, result.getTitle());
    }

  }


  @Nested
  @DisplayName("예매취소 로직 테스트")
  class testDeleteReservation{
    Long testReservationId = 888888L;
    Long testTicketInfoId = 123456L;
    int count = 1;
    int initialValue = 100;
    AtomicInteger leftSeats = new AtomicInteger(initialValue);

    @Test
    @DisplayName("오늘이 공연일이라 예매 취소 실패")
    void test6() {
//      given
      when(reservationRepository.findById(testReservationId)).thenReturn(Optional.of(testReservation));
      when(testReservation.getUser()).thenReturn(testUser);
      when(testReservation.getTicketInfoId()).thenReturn(testTicketInfoId);
      when(ticketInfoRepository.findById(testTicketInfoId)).thenReturn(Optional.of(testTicketInfo));
      when(testTicketInfo.getEvent()).thenReturn(testEvent);
      when(testEvent.getDate()).thenReturn(LocalDateTime.now());
//      when
      try {
        reservationService.deleteReservation(testReservationId, testUser);
      } catch (CustomException e) {
        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
      }
    }

    @Test
    @DisplayName("예매 취소 성공 DB로")
    void test7(){
//      given
      when(reservationRepository.findById(testReservationId)).thenReturn(Optional.of(testReservation));
      when(testReservation.getUser()).thenReturn(testUser);
      when(testReservation.getTicketInfoId()).thenReturn(testTicketInfoId);
      when(testReservation.getCount()).thenReturn(count);
      when(ticketInfoRepository.findById(testTicketInfoId)).thenReturn(Optional.of(testTicketInfo));
      when(testTicketInfo.getEvent()).thenReturn(testEvent);
      when(testTicketInfo.getLeftSeats()).thenAnswer(invocation -> leftSeats.get());
      when(testTicketInfo.getId()).thenReturn(testTicketInfoId);
      when(testEvent.getDate()).thenReturn(LocalDateTime.now().plusMonths(1));
      when(redisRepository.hasLeftSeatsInRedis(testTicketInfoId)).thenReturn(false);
      doAnswer(invocation -> {
        int invokedCount = invocation.getArgument(0, Integer.class);
        leftSeats.addAndGet(invokedCount);
        return null;
      }).when(testTicketInfo).plusSeats(anyInt());
//      when
      reservationService.deleteReservation(testReservationId, testUser);
//      then
      assertEquals(initialValue+count, testTicketInfo.getLeftSeats());
      verify(redisRepository, never()).incrementLeftSeatInRedis(testTicketInfoId, count);
    }

  }

}