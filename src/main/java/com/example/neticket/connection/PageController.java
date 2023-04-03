package com.example.neticket.connection;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  @GetMapping("/events-page")
  public ModelAndView mainPage() {
    return new ModelAndView("index");
  }

  // 상세페이지 이동
  @GetMapping("/events/detail/{eventId}")
  public ModelAndView detailPage(@PathVariable Long eventId) {
    Map<String, Object> modelData = new HashMap<>();
    modelData.put("eventId",eventId);
    return new ModelAndView("detail", modelData);
  }

  // 예매 중 이동
  @GetMapping("/reservations/in-progress/{ticketInfoId}")
  public ModelAndView myPage(@PathVariable Long ticketInfoId) {
    Map<String, Object> modelData = new HashMap<>();
    modelData.put("ticketInfoId",ticketInfoId);
    return new ModelAndView("reservation", modelData);
  }

  // 예약완료 페이지 이동
  @GetMapping("/reservations/completed/{resvId}")
  public ModelAndView reservationsPage() {
    return new ModelAndView("completed");
  }
}
