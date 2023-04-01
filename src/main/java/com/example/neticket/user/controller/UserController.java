package com.example.neticket.user.controller;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.user.dto.LoginRequestDto;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/neticket")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/signup")
  public String signup(@RequestBody @Valid SignupRequestDto dto) {

    userService.signup(dto);

    return "redirect:/neticket/login-page";
  }

  // 로그인
  @PostMapping("/login")
  public @ResponseBody MessageResponseDto login (@RequestBody @Valid LoginRequestDto dto, HttpServletResponse response){

    userService.login(dto, response);

    return new MessageResponseDto(HttpStatus.OK, "로그인이 완료되었습니다.");
  }

}
