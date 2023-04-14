package com.example.neticket.reservation.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
    TicketInfo ticketInfo = checkAndMinusLeftSeat(dto);
    if (ticketInfo == null) {
      System.out.println("티켓인포가 null!!");
    }
    return reservationRepository.saveAndFlush(new Reservation(dto, user, ticketInfo)).getId();
  }

//   남은 좌석 수 차감. 예매하기의 자식 트랜잭션.
  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
  public TicketInfo checkAndMinusLeftSeat(ReservationRequestDto dto) {
    TicketInfo ticketInfo = ticketInfoRepository.findByIdWithLock(dto.getTicketInfoId())
        .orElseThrow(
            () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
        );
    if (!ticketInfo.isAvailable()) {
      throw new CustomException(ExceptionType.RESERVATION_UNAVAILABLE_EXCEPTION);
    }
    if (0 > ticketInfo.getLeftSeats() - dto.getCount()) {
      throw new CustomException(ExceptionType.OUT_OF_TICKET_EXCEPTION);
    }
    ticketInfo.minusSeats(dto.getCount());
    return ticketInfoRepository.save(ticketInfo);
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

//    공연날이 오늘이거나 오늘보다 이전이면 예매 취소 불가능
    LocalDate eventDay = LocalDate.from(reservation.getTicketInfo().getEvent().getDate());
    if(LocalDate.now().isAfter(eventDay) || LocalDate.now().equals(eventDay)){
      throw new CustomException(ExceptionType.CANCEL_DEADLINE_PASSED_EXCEPTION);
    }

    // 예매 취소 후 좌석 수 업데이트
    reservation.getTicketInfo().plusSeats(reservation.getCount());
    reservationRepository.delete(reservation);

  }

//  예매 기록의 사용자와 현재 토큰상의 사용자 일치 여부 판별 메서드
  private void checkReservationUser(Reservation reservation, User user) {
    if (!reservation.getUser().getId().equals(user.getId())) {
      throw new CustomException(ExceptionType.USER_RESERVATION_NOT_MATCHING_EXCEPTION);
    }
  }



}

