package com.example.neticket.user.controller;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.security.UserDetailsImpl;
import com.example.neticket.user.dto.LoginRequestDto;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.service.UserService;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/neticket")
public class UserController {

  private final UserService userService;

  // 회원가입 이동
  @GetMapping("/signup")
  public ModelAndView signupPage() {
    return new ModelAndView("signup");
  }

  // 로그인 이동
  @GetMapping("/login")
  public ModelAndView loginPage() {
    return new ModelAndView("login");
  }

  // 마이페이지 이동
  @GetMapping("/mypages")
  public ModelAndView myPage() {return new ModelAndView("mypages");}

  // 회원가입
  @PostMapping("/signup")
  public String signup(@RequestBody @Valid SignupRequestDto dto) {

    userService.signup(dto);

    return "redirect:/neticket/signup";
  }

  // 로그인
  @PostMapping("/login")
  public String login (@RequestBody @Valid LoginRequestDto dto, HttpServletResponse response){

    userService.login(dto, response);

    return "redirect:/neticket/login";
  }




}
