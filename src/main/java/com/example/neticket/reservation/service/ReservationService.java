package com.example.neticket.reservation.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.RedisRepository;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.entity.User;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final TicketInfoRepository ticketInfoRepository;
  private final RedisRepository redisRepository;

  // 1. 예매중 페이지에서 공연정보 조회
  @Cacheable(value = "DetailEventResponseDto", key = "#ticketInfoId", cacheManager = "cacheManager")
  @Transactional(readOnly = true)
  public DetailEventResponseDto verifyReservation(Long ticketInfoId) {
    return ticketInfoRepository.findById(ticketInfoId)
        .map(ticketInfo -> new DetailEventResponseDto(ticketInfo.getEvent()))
        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
  }

  // 2. 예매하기
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Long makeReservation(ReservationRequestDto dto, User user) {
    //    먼저 redis 캐시를 조회
    Integer leftSeats = redisRepository.findLeftSeatsFromRedis(dto.getTicketInfoId());
    if (leftSeats == null) {
    //    캐시가 없으면 DB를 통해 남은 좌석 수 차감
      decrementLeftSeatInDB(dto);
    } else {
    //    캐시가 있으면 redis에서 남은 좌석 수 차감
      decrementLeftSeatInRedis(dto, leftSeats);
    }

    Reservation reservation = new Reservation(dto, user);
    reservationRepository.saveAndFlush(reservation);
    return reservation.getId();
  }

  //  2-1. redis로 좌석 수 변경
  private void decrementLeftSeatInRedis(ReservationRequestDto dto, Integer leftSeats) {
    if (leftSeats - dto.getCount() < 0) {
      throw new CustomException(ExceptionType.OUT_OF_TICKET_EXCEPTION);
    }
    redisRepository.decrementLeftSeatInRedis(dto.getTicketInfoId(), dto.getCount());
  }

  // 2-2. 캐시 없으면 DB로 좌석수 변경
  private void decrementLeftSeatInDB(ReservationRequestDto dto) {
    TicketInfo ticketInfo = ticketInfoRepository.findById(dto.getTicketInfoId())
        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
    if (!ticketInfo.isAvailable()) {
      throw new CustomException(ExceptionType.RESERVATION_UNAVAILABLE_EXCEPTION);
    }
    if (ticketInfo.getLeftSeats() - dto.getCount() < 0) {
      throw new CustomException(ExceptionType.OUT_OF_TICKET_EXCEPTION);
    }
    ticketInfo.minusSeats(dto.getCount());
    ticketInfoRepository.save(ticketInfo);
  }


  // 3. 예매완료
  @Transactional(readOnly = true)
  public ReservationResponseDto reservationComplete(Long resvId, User user) {
    Reservation reservation = reservationRepository.findById(resvId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION)
    );
    TicketInfo ticketInfo = ticketInfoRepository.findById(reservation.getTicketInfoId()).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
    );

    checkReservationUser(reservation, user);

    return new ReservationResponseDto(reservation, ticketInfo);
  }

  // 4. 예매취소 여기에도 캐시 처리 해야
  @Transactional
  public void deleteReservation(Long resvId, User user) {
    Reservation reservation = reservationRepository.findById(resvId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION)
    );
    checkReservationUser(reservation, user);
    TicketInfo ticketInfo = ticketInfoRepository.findById(reservation.getTicketInfoId()).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
    );

//    공연날이 오늘이거나 오늘보다 이전이면 예매 취소 불가능
    LocalDate eventDay = LocalDate.from(ticketInfo.getEvent().getDate());
    if(LocalDate.now().isAfter(eventDay) || LocalDate.now().equals(eventDay)){
      throw new CustomException(ExceptionType.CANCEL_DEADLINE_PASSED_EXCEPTION);
    }

    // 예매 취소 후 좌석 수 업데이트
    ticketInfo.plusSeats(reservation.getCount());
    reservationRepository.delete(reservation);

  }

//  3-1, 4-1. 예매 기록의 사용자와 현재 토큰상의 사용자 일치 여부 판별 메서드
  private void checkReservationUser(Reservation reservation, User user) {
    if (!reservation.getUser().getId().equals(user.getId())) {
      throw new CustomException(ExceptionType.USER_RESERVATION_NOT_MATCHING_EXCEPTION);
    }
  }

  //  5. ADMIN. DB에서 남은 좌석수만 가져와서 Redis에 (key-value)형태로 저장
  @Transactional
  public MessageResponseDto saveLeftSeatsInRedis(Long ticketInfoId) {
    TicketInfo ticketInfo = ticketInfoRepository.findById(ticketInfoId)
        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
    redisRepository.saveTicketInfoToRedis(ticketInfo);
    return new MessageResponseDto(HttpStatus.CREATED, "redis에 성공적으로 저장되었습니다.");
  }

  //  6. ADMIN. 해당하는 공연의 남은 좌석수 Redis에서 삭제(삭제되기전 모든 캐시 DB에 반영)
  @Transactional
  public MessageResponseDto deleteLeftSeatsFromRedis(Long ticketInfoId) {
    redisRepository.deleteLeftSeatsInRedis(ticketInfoId);
    return new MessageResponseDto(HttpStatus.OK, "redis에서 캐시가 성공적으로 삭제되었습니다.");
  }



}

