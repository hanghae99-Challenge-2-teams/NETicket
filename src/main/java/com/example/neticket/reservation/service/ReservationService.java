package com.example.neticket.reservation.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final TicketInfoRepository ticketInfoRepository;

  // 예매하기
  @Transactional
  public Long makeReservations(ReservationRequestDto dto, User user) {
    TicketInfo ticketInfo = ticketInfoRepository.findById(dto.getTicketInfoId()).orElseThrow(
        () -> new IllegalArgumentException("공연회차 정보가 없습니다.")
    );
    if (!ticketInfo.isAvailable()) {
      throw new IllegalArgumentException("현재 예매가 불가능한 공연입니다.");
    }

    if (ticketInfo.getTotalSeats() >= ticketInfo.getReservedSeats() + dto.getCount()) {
      ticketInfo.reserveSeats(dto.getCount());

      // 만약 ReservedSeats와 TotalSeats가 같아지면 isAvailable을 false로 변경
      if (ticketInfo.getTotalSeats() == ticketInfo.getReservedSeats()) {
        ticketInfo.setAvailable(false);
      }
      ticketInfoRepository.save(ticketInfo);
      Reservation reservation = new Reservation(dto, user, ticketInfo);
      reservationRepository.saveAndFlush(reservation);
      return reservation.getId();
    }
    throw new IllegalArgumentException("남은 자리가 없습니다");
  }

  // 예매중 페이지에서 공연정보 조회
  @Transactional(readOnly = true)
  public DetailEventResponseDto reservation(Long ticketInfoId) {
    return ticketInfoRepository.findById(ticketInfoId)
        .map(ticketInfo -> new DetailEventResponseDto(ticketInfo.getEvent()))
        .orElseThrow(() -> new IllegalArgumentException("공연 정보가 없습니다."));
  }


  // 예매완료
  @Transactional(readOnly = true)
  public ReservationResponseDto reservationComplete(Long resvId, User user) {

    Reservation reservation = reservationRepository.findById(resvId).orElseThrow(
        () -> new IllegalArgumentException("예매한 공연이 없습니다.")
    );

    if (!reservation.getUser().getId().equals(user.getId())) {
      throw new IllegalArgumentException("예약정보가 회원정보와 일치하지 않습니다.");
    }

    return new ReservationResponseDto(reservation);
  }


}