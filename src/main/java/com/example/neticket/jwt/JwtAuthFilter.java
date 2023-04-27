package com.example.neticket.jwt;

import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = jwtUtil.resolveToken(request);
    if (token != null) {
      if (!jwtUtil.validateToken(token)) {
        throw new CustomException(ExceptionType.TOKEN_VALIDATION_EXCEPTION);
      }
      Claims info = jwtUtil.getUserInfoFromToken(token);
      setAuthentication(info.getSubject());
    }
    filterChain.doFilter(request, response);
  }

  public void setAuthentication(String email) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = jwtUtil.createAuthentication(email);
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

}
