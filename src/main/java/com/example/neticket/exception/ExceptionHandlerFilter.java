package com.example.neticket.exception;

import com.example.neticket.event.dto.MessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) {
    try {
      filterChain.doFilter(request, response);
    } catch (CustomException e) {
      jwtExceptionHandler(response, e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (RuntimeException e) {
      jwtExceptionHandler(response, e.getMessage().split(":")[0], HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      jwtExceptionHandler(response, e.getMessage().split(":")[0], HttpStatus.BAD_REQUEST);
    }
  }

  public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus httpStatus) {
    response.setStatus(httpStatus.value());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    try {
      String json = new ObjectMapper().writeValueAsString(new MessageResponseDto(httpStatus, msg));
      response.getWriter().write(json);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
