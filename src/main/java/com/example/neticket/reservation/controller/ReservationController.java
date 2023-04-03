package com.example.neticket.reservation.controller;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.service.ReservationService;
import com.example.neticket.security.UserDetailsImpl;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/neticket")
public class ReservationController {

  private final ReservationService reservationService;

  // 예매중 확인하기
  @GetMapping("/reservation/{ticketInfoId}")
  public @ResponseBody DetailEventResponseDto reservation(@PathVariable Long ticketInfoId) {
    return reservationService.reservation(ticketInfoId);
  }

  // 예매하기
  @ResponseBody
  @PostMapping("/resv")
  public Map<String, Object> makeReservations(@RequestBody ReservationRequestDto dto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Long resvId = reservationService.makeReservations(dto, userDetails.getUser());
    Map<String, Object> response = new HashMap<>();
    response.put("success", "success");
    response.put("reservationId", resvId);
    return response;
  }

  // 예매완료
  @GetMapping("/resv/{resvId}")
  public @ResponseBody ReservationResponseDto reservationComplete(@PathVariable Long resvId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return reservationService.reservationComplete(resvId, userDetails.getUser());

  }

}
