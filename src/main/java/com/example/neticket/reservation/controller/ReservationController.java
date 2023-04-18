package com.example.neticket.reservation.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.service.ReservationService;
import com.example.neticket.security.UserDetailsImpl;
import java.util.List;
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

  // 예매중 확인하기
  @GetMapping("/ticket-info/{ticketInfoId}")
  public ResponseEntity<DetailEventResponseDto> verifyReservation(@PathVariable Long ticketInfoId) {
    DetailEventResponseDto reservation = reservationService.verifyReservation(ticketInfoId);
    return ResponseEntity.ok().body(reservation);
  }

  // 예매하기
  @PostMapping("/reservations")
  public ResponseEntity<Long> makeReservations(
      @RequestBody @Valid ReservationRequestDto dto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Long resvId = reservationService.makeReservation(dto, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(resvId);
  }

  // 예매완료
  @GetMapping("/reservations/{resvId}")
  public ResponseEntity<ReservationResponseDto> reservationComplete(
      @PathVariable Long resvId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReservationResponseDto responseDto = reservationService.reservationComplete(resvId, userDetails.getUser());
    return ResponseEntity.ok().body(responseDto);

  }

  // 예매취소
  @DeleteMapping("/reservations/{resvId}")
  public ResponseEntity<MessageResponseDto> deleteReservation(@PathVariable Long resvId, @AuthenticationPrincipal UserDetailsImpl userDetails){
    reservationService.deleteReservation(resvId, userDetails.getUser());
    return ResponseEntity.ok(new MessageResponseDto(HttpStatus.OK, "예매 기록이 성공적으로 삭제되었습니다."));
  }

  //  ADMIN. DB에서 남은 좌석수만 가져와서 Redis에 (key-value)형태로 저장
  @PostMapping("/cache/left-seats/{ticketInfoId}")
  public MessageResponseDto saveLeftSeatsInRedis(@PathVariable Long ticketInfoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return reservationService.saveLeftSeatsInRedis(ticketInfoId, userDetails.getUser());
  }

  //  ADMIN. 해당하는 공연의 남은 좌석수 Redis에서 삭제(삭제되기전 모든 캐시 DB에 반영)
  @DeleteMapping("/cache/left-seats/{ticketInfoId}")
  public MessageResponseDto deleteLeftSeatsFromRedis(@PathVariable Long ticketInfoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return reservationService.deleteLeftSeatsFromRedis(ticketInfoId, userDetails.getUser());
  }

  //  ADMIN. Redis에 등록된 모든 leftSeats 키값 리스트 반환
  @GetMapping("/cache/left-seats")
  public List<String> findAllLeftSeatsKeysInRedis(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return reservationService.findAllLeftSeatsKeysInRedis(userDetails.getUser());
  }

}
