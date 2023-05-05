package com.example.neticket.user.controller;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.security.UserDetailsImpl;
import com.example.neticket.user.dto.LoginRequestDto;
import com.example.neticket.user.dto.LoginResponseDto;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.service.UserService;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/neticket")
public class UserController {

  private final UserService userService;

  // 1.회원가입
  @PostMapping("/signup")
  public ResponseEntity<MessageResponseDto> signup(@RequestBody @Valid SignupRequestDto dto) {
    MessageResponseDto signup = userService.signup(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(signup);
  }

  // 2.로그인
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto,
      HttpServletResponse response) {
    LoginResponseDto login = userService.login(dto, response);
    return ResponseEntity.ok().body(login);
  }

  // 3.마이페이지
  @GetMapping("/user")
  public ResponseEntity<List<ReservationResponseDto>> getUserInfo(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<ReservationResponseDto> userReservationList = userService.getUserInfo(
        userDetails.getUser());
    return ResponseEntity.ok().body(userReservationList);
  }

}
