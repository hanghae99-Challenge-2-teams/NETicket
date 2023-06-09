package com.example.neticket.user.service;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.jwt.JwtUtil;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.dto.LoginRequestDto;
import com.example.neticket.user.dto.LoginResponseDto;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final ReservationRepository reservationRepository;
  private final TicketInfoRepository ticketInfoRepository;

  // 1.회원가입
  @Transactional
  public MessageResponseDto signup(SignupRequestDto dto) {

    boolean isEmailExist = userRepository.existsByEmail(dto.getEmail());
    if (isEmailExist) {
      throw new CustomException(ExceptionType.EXISTED_EMAIL_EXCEPTION);
    }

    boolean isNickExist = userRepository.existsByNickname(dto.getNickname());
    if (isNickExist) {
      throw new CustomException(ExceptionType.EXISTED_NICKNAME_EXCEPTION);
    }

    String password = passwordEncoder.encode(dto.getPassword());

    User user = new User(dto, password);
    userRepository.saveAndFlush(user);

    return new MessageResponseDto(HttpStatus.CREATED, "회원가입이 완료되었습니다.");
  }

  // 2.로그인
  @Transactional(readOnly = true)
  public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response) {

    User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_USER_EXCEPTION)
    );

    String encodePassword = user.getPassword();
    if (!passwordEncoder.matches(dto.getPassword(), encodePassword)) {
      throw new CustomException(ExceptionType.PASSWORD_NOT_MATCHING_EXCEPTION);
    }

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER,
        jwtUtil.createToken(user.getEmail(), user.getNickname(), user.getRole()));

    return new LoginResponseDto(user);
  }

  // 3.마이페이지 조회
  @Transactional(readOnly = true)
  public List<ReservationResponseDto> getUserInfo(User user) {
    return reservationRepository.findAllByUserOrderByIdDesc(user)
        .stream()
        .map(reservation -> {
          TicketInfo ticketInfo = ticketInfoRepository.findById(reservation.getTicketInfoId())
              .orElseThrow(
                  () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
          return new ReservationResponseDto(reservation, ticketInfo);
        })
        .collect(Collectors.toList());
  }

}

