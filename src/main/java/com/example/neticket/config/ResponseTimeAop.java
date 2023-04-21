package com.example.neticket.config;

import com.example.neticket.security.UserDetailsImpl;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ResponseTimeAop {

  @Around("execution(public * com.example.neticket.reservation.controller.ReservationController.makeReservations(..)) && args(.., userDetails)")
  public Object checkResponseTime(ProceedingJoinPoint joinPoint, UserDetailsImpl userDetails) throws Throwable{

    boolean isAdmin = userDetails.getAuthorities().stream()
        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin) return joinPoint.proceed();

    long startTime = System.currentTimeMillis();

    try {
      return joinPoint.proceed();
    } finally {
      long responseTime = System.currentTimeMillis() - startTime;
//      responseTime을 http response header에 "X-Response-Time" 키값에다가 value로 삽입해서 반환
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (requestAttributes != null) {
        HttpServletResponse response = requestAttributes.getResponse();
        if (response != null) {
          response.setHeader("X-Response-Time", String.valueOf(responseTime));
        }
      }
    }

  }

}
