package com.example.neticket.user.controller;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.security.UserDetailsImpl;
import com.example.neticket.user.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/neticket")
public class AdminController {

  private final AdminService adminService;

  // 1. ADMIN. DB에서 남은 좌석수만 가져와서 Redis에 (key-value)형태로 저장
  @PostMapping("/cache/left-seats/{ticketInfoId}")
  public MessageResponseDto saveLeftSeatsInRedis(@PathVariable Long ticketInfoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return adminService.saveLeftSeatsInRedis(ticketInfoId, userDetails.getUser());
  }

  // 2. ADMIN. 해당하는 공연의 남은 좌석수 Redis에서 삭제(삭제되기전 모든 캐시 DB에 반영)
  @DeleteMapping("/cache/left-seats/{ticketInfoId}")
  public MessageResponseDto deleteLeftSeatsFromRedis(@PathVariable Long ticketInfoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return adminService.deleteLeftSeatsFromRedis(ticketInfoId, userDetails.getUser());
  }

  // 3. ADMIN. Redis에 등록된 모든 leftSeats 키값 리스트 반환
  @GetMapping("/cache/left-seats")
  public List<String> findAllLeftSeatsKeysInRedis(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return adminService.findAllLeftSeatsKeysInRedis(userDetails.getUser());
  }

  // 4. ADMIN. 해당 ticketInfoId에 해당하는 남은 좌석 수를 정확히 세서 DB와 Redis에 refresh.
  @PatchMapping("/cache/left-seats/{ticketInfoId}")
  public MessageResponseDto refreshLeftSeats(@PathVariable Long ticketInfoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return adminService.refreshLeftSeats(ticketInfoId, userDetails.getUser());
  }

}
