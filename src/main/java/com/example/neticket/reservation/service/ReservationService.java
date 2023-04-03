package com.example.neticket.reservation.service;

import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
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

     if (ticketInfo.getTotalSeats() >= ticketInfo.getReservedSeats() + dto.getCount()) {
       ticketInfo.reserveSeats(dto.getCount());
       Reservation reservation = new Reservation(dto, user, ticketInfo);
       reservationRepository.saveAndFlush(reservation);
       return reservation.getId();
     }
     throw new IllegalArgumentException("남은 자리가 없습니다");
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

  // 마이페이지
//  @Transactional(readOnly = true)
//  public List<ReservationResponseDto> getMyPage(User user) {
//
//    List<Reservation> allByUser = reservationRepository.findAllByUser(user);
//    List<ReservationResponseDto> dtoList = allByUser.stream()
//        .map(ReservationResponseDto::new)
//        .collect(Collectors.toList());
//
//    return dtoList;
//  }
}