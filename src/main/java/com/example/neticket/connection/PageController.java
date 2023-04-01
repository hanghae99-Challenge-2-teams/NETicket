package com.example.neticket.connection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/neticket")
public class PageController {

  // 회원가입 이동
  @GetMapping("/signup-page")
  public ModelAndView signupPage() {
    return new ModelAndView("signup");
  }

  // 로그인 이동
  @GetMapping("/login-page")
  public ModelAndView loginPage() {
    return new ModelAndView("login");
  }

  // 메인페이지 이동
  @GetMapping("/events-page")
  public ModelAndView mainPage() {
    return new ModelAndView("index");
  }

  // 메인페이지 이동
  @GetMapping("/events/detail-page")
  public ModelAndView detailPage() {
    return new ModelAndView("detail");
  }

  // 예약완료 페이지 이동
  @GetMapping("/reservations-page")
  public ModelAndView reservationsPage() {
    return new ModelAndView("mypage");
  }

  // 마이페이지 이동
  @GetMapping("/mypage-page")
  public ModelAndView myPage() {
    return new ModelAndView("mypage");
  }

}
