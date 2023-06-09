package com.example.neticket.reservation.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.service.ReservationService;
import com.example.neticket.security.UserDetailsImpl;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/neticket")
public class ReservationController {

  private final ReservationService reservationService;

  // 1.예매중 확인하기
  @GetMapping("/ticket-info/{ticketInfoId}")
  public ResponseEntity<DetailEventResponseDto> verifyReservation(@PathVariable Long ticketInfoId) {
    DetailEventResponseDto detailEventResponseDto = reservationService.verifyReservation(
        ticketInfoId);
    return ResponseEntity.ok().body(detailEventResponseDto);

  }

  // 2.예매하기
  @PostMapping("/reservations")
  public ResponseEntity<Long> makeReservations(
      @RequestBody @Valid ReservationRequestDto dto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Long reservationId = reservationService.makeReservation(dto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(reservationId);

  }

  // 3.예매완료
  @GetMapping("/reservations/{reservationId}")
  public ResponseEntity<ReservationResponseDto> reservationComplete(
      @PathVariable Long reservationId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReservationResponseDto detailEventResponseDto = reservationService.reservationComplete(
        reservationId, userDetails.getUser());
    return ResponseEntity.ok().body(detailEventResponseDto);

  }

  // 4.예매취소
  @DeleteMapping("/reservations/{resvId}")
  public ResponseEntity<MessageResponseDto> deleteReservation(@PathVariable Long resvId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    reservationService.deleteReservation(resvId, userDetails.getUser());
    return ResponseEntity.ok(new MessageResponseDto(HttpStatus.OK, "예매 기록이 성공적으로 삭제되었습니다."));
  }

}
