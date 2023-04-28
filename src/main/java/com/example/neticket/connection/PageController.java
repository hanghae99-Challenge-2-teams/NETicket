package com.example.neticket.connection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/neticket")
public class PageController {

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

  // 메인페이지 이동
  @GetMapping
  public ModelAndView mainPage() {
    return new ModelAndView("index");
  }

  // 상세페이지 이동
  @GetMapping("/events/{eventId}")
  public ModelAndView detailPage() {
    return new ModelAndView("detail");
  }

  // 예매중 이동
  @GetMapping("/reservations/in-progress/{ticketInfoId}")
  public ModelAndView reservation() {
    return new ModelAndView("reservation");
  }

  // 예약완료 페이지 이동
  @GetMapping("/reservations/completed/{resvId}")
  public ModelAndView reservationConfirmed() {
    return new ModelAndView("completed");
  }

  // 관리자 공연 추가 페이지 이동
  @GetMapping("/admin/event")
  public ModelAndView addEvent() {
    return new ModelAndView("addevent");
  }

  // 사용자 마이페이지
  @GetMapping("/user")
  public ModelAndView myPage() {
      return new ModelAndView("mypage");
  }

  // 검색 페이지 이동
  @GetMapping("/search")
  public ModelAndView search() { return  new ModelAndView("search");}

}
