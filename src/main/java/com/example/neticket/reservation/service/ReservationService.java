package com.example.neticket.reservation.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final TicketInfoRepository ticketInfoRepository;

  // 예매중 페이지에서 공연정보 조회
  @Transactional(readOnly = true)
  public DetailEventResponseDto verifyReservation(Long ticketInfoId) {
    return ticketInfoRepository.findById(ticketInfoId)
        .map(ticketInfo -> new DetailEventResponseDto(ticketInfo.getEvent()))
        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
  }

  // 예매하기
  @Transactional
  public Long makeReservation(ReservationRequestDto dto, User user) {
    TicketInfo ticketInfo = ticketInfoRepository.findByIdWithLock(dto.getTicketInfoId())
        .orElseThrow(
            () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
        );
    if (!ticketInfo.isAvailable()) {
      throw new CustomException(ExceptionType.RESERVATION_UNAVAILABLE_EXCEPTION);
    }
    if (0 <= ticketInfo.getLeftSeats() - dto.getCount()) {
      int getLeftSeats = ticketInfo.minusSeats(dto.getCount());

      // 만약 ReservedSeats와 TotalSeats가 같아지면 isAvailable을 false로 변경
      if (getLeftSeats == 0) {
        ticketInfo.setAvailable(false);
      }
      ticketInfoRepository.save(ticketInfo);
      Reservation reservation = new Reservation(dto, user, ticketInfo);
      reservationRepository.saveAndFlush(reservation);
      return reservation.getId();
    }
    throw new CustomException(ExceptionType.OUT_OF_TICKET_EXCEPTION);
  }

  // 예매완료
  @Transactional(readOnly = true)
  public ReservationResponseDto reservationComplete(Long resvId, User user) {

    Reservation reservation = reservationRepository.findById(resvId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION)
    );

    checkReservationUser(reservation, user);

    return new ReservationResponseDto(reservation);
  }

  // 예매취소
  @Transactional
  public void deleteReservation(Long resvId, User user) {
    Reservation reservation = reservationRepository.findById(resvId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION)
    );

    checkReservationUser(reservation, user);

    LocalDate deadline = LocalDate.from(reservation.getTicketInfo().getEvent().getDate());
    if(LocalDate.now().isBefore(deadline) || LocalDate.now().equals(deadline)){
      throw new CustomException(ExceptionType.CANCEL_DEADLINE_PASSED_EXCEPTION);
    }

    // 예매 취소 후 좌석 수 업데이트
    reservation.getTicketInfo().plusSeats(reservation.getCount());

    reservationRepository.delete(reservation);

  }

  private void checkReservationUser(Reservation reservation, User user) {
    if (!reservation.getUser().getId().equals(user.getId())) {
      throw new CustomException(ExceptionType.USER_RESERVATION_NOT_MATCHING_EXCEPTION);
    }
  }



}

