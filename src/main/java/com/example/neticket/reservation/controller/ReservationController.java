package com.example.neticket.reservation.controller;

import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.service.ReservationService;
import com.example.neticket.security.UserDetailsImpl;
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

  // 예매하기
  @PostMapping("/resv")
  public String makeReservations(@RequestBody ReservationRequestDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Long resvId = reservationService.makeReservations(dto, userDetails.getUser());
    return "redirect:/neticket/reservations/completed/" + resvId;
  }

  // 예매완료
  @GetMapping("/resv/{resvId}")
  public @ResponseBody ReservationResponseDto reservationComplete(@PathVariable Long resvId, @AuthenticationPrincipal UserDetailsImpl userDetails){
    return reservationService.reservationComplete(resvId, userDetails.getUser());

  }

  // 마이페이지
//  @GetMapping("/mypage")
//  public List<ReservationResponseDto> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//    return reservationService.getMyPage(userDetails.getUser());
//
//  }



}
