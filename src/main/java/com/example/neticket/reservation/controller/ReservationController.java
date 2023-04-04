package com.example.neticket.reservation.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.service.ReservationService;
import com.example.neticket.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/neticket")
public class ReservationController {

  private final ReservationService reservationService;

  // 예매중 확인하기
  @GetMapping("/reservation/{ticketInfoId}")
  public ResponseEntity<DetailEventResponseDto> reservation(@PathVariable Long ticketInfoId) {
    DetailEventResponseDto reservation = reservationService.reservation(ticketInfoId);
    return ResponseEntity.ok().body(reservation);
  }

  // 예매하기
  @PostMapping("/resv")
  public ResponseEntity<Long> makeReservations(
      @RequestBody ReservationRequestDto dto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Long resvId = reservationService.makeReservations(dto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(resvId);
  }

  // 예매완료
  @GetMapping("/resv/{resvId}")
  public ResponseEntity<ReservationResponseDto> reservationComplete(
      @PathVariable Long resvId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReservationResponseDto responseDto = reservationService.reservationComplete(resvId, userDetails.getUser());
    return ResponseEntity.ok().body(responseDto);

  }

}
