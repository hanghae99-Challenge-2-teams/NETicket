package com.example.neticket.reservation.service;

import com.example.neticket.event.entity.ShowTime;
import com.example.neticket.event.repository.ShowTimeRepository;
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
  private final ShowTimeRepository showTimeRepository;

  // 예매하기
  @Transactional
  public void makeReservations(ReservationRequestDto dto, User user) {
    ShowTime showTime = showTimeRepository.findById(dto.getShowTimeId()).orElseThrow(
        () -> new IllegalArgumentException("공연회차 정보가 없습니다.")
    );

    reservationRepository.saveAndFlush(new Reservation(dto, user, showTime));
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
  @Transactional(readOnly = true)
  public List<ReservationResponseDto> getMyPage(User user) {

    List<Reservation> allByUser = reservationRepository.findAllByUser(user);
    List<ReservationResponseDto> dtoList = allByUser.stream()
        .map(ReservationResponseDto::new)
        .collect(Collectors.toList());

    return dtoList;
  }
}