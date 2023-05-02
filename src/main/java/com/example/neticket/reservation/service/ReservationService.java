package com.example.neticket.reservation.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.EventRepository;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.RedisRepository;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.entity.User;
import java.time.Duration;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

  private final EventRepository eventRepository;
  private final ReservationRepository reservationRepository;
  private final TicketInfoRepository ticketInfoRepository;
  private final RedisRepository redisRepository;
  private final RedisTemplate<String, DetailEventResponseDto> redisTemplate;

  // 1.예매중 페이지에서 공연정보 조회
  @Transactional(readOnly = true)
  public DetailEventResponseDto verifyReservation(Long eventId) {
    String cacheKey = "DetailEventResponseDto::" + eventId;
    DetailEventResponseDto detailEventResponseDto = null;

    try {
      detailEventResponseDto = redisTemplate.opsForValue().get(cacheKey);
      if (detailEventResponseDto == null) {
        detailEventResponseDto = getEventInfo(eventId);
        redisTemplate.opsForValue().set(cacheKey, detailEventResponseDto, Duration.ofHours(1));
      }
    } catch (Exception e) {
      detailEventResponseDto = getEventInfo(eventId);
    }
    return detailEventResponseDto;

  }

  // 2.예매하기
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Long makeReservation(ReservationRequestDto dto, User user) {
    try {
      Boolean success = redisRepository.decrementLeftSeatInRedis(dto.getTicketInfoId(),
          dto.getCount());
      if (!success) {
        throw new CustomException(ExceptionType.OUT_OF_TICKET_EXCEPTION);
      }
    } catch (Exception e) {
      if (e instanceof CustomException) {
        throw e;
      }
      decrementLeftSeatInDB(dto);
    }
    return reservationRepository.save(new Reservation(dto, user)).getId();

  }

  // 2-1.캐시 없으면 DB로 좌석수 변경
  private void decrementLeftSeatInDB(ReservationRequestDto dto) {
    TicketInfo ticketInfo = ticketInfoRepository.findByIdWithLock(dto.getTicketInfoId())
        .orElseThrow(
            () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
        );
    ticketInfo.minusSeats(dto.getCount());
    ticketInfoRepository.save(ticketInfo);
  }

  // 3.예매완료
  @Transactional(readOnly = true)
  public ReservationResponseDto reservationComplete(Long reservationId, User user) {
    Reservation reservation = checkReservationById(reservationId);
    checkReservationUser(reservation, user);
    TicketInfo ticketInfo = checkTicketInfoById(reservation.getTicketInfoId());
    return new ReservationResponseDto(reservation, ticketInfo);

  }

  // 4.예매취소
  @Transactional
  public void deleteReservation(Long reservationId, User user) {
    Reservation reservation = checkReservationById(reservationId);
    checkReservationUser(reservation, user);
    TicketInfo ticketInfo = checkTicketInfoById(reservation.getTicketInfoId());

    LocalDate eventDay = LocalDate.from(ticketInfo.getEvent().getDate());
    if (!LocalDate.now().isBefore(eventDay)) {
      throw new CustomException(ExceptionType.CANCEL_DEADLINE_PASSED_EXCEPTION);
    }

    boolean hasLeftSeats = redisRepository.hasLeftSeatsInRedis(ticketInfo.getId());
    if (hasLeftSeats) {
      redisRepository.incrementLeftSeatInRedis(ticketInfo.getId(), reservation.getCount());
    } else {
      ticketInfo.plusSeats(reservation.getCount());
    }
    reservationRepository.delete(reservation);

  }

  // 1-1. eventId 로 공연 조회
  private DetailEventResponseDto getEventInfo(Long eventId) {
    return eventRepository.findById(eventId).map(DetailEventResponseDto::new)
        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_EVENT_EXCEPTION));
  }

  //  3-1. 예매 기록의 사용자와 현재 토큰상의 사용자 일치 여부 판별 메서드
  private void checkReservationUser(Reservation reservation, User user) {
    if (!reservation.getUser().getId().equals(user.getId())) {
      throw new CustomException(ExceptionType.USER_RESERVATION_NOT_MATCHING_EXCEPTION);
    }
  }

  //  3-2. 예매 ID로 Reservation 확인
  private Reservation checkReservationById(Long reservationId) {
    return reservationRepository.findById(reservationId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION)
    );
  }

  //  3-3. ticketInfoId로 TicketInfo 확인
  private TicketInfo checkTicketInfoById(Long ticketInfoId) {
    return ticketInfoRepository.findById(ticketInfoId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
    );
  }

}

