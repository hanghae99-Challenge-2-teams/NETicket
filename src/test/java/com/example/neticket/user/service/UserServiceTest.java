package com.example.neticket.user.service;

import static org.mockito.Mockito.*;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.jwt.JwtUtil;
import com.example.neticket.reservation.dto.ReservationResponseDto;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.reservation.repository.ReservationRepository;
import com.example.neticket.user.dto.LoginRequestDto;
import com.example.neticket.user.dto.LoginResponseDto;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.entity.UserRoleEnum;
import com.example.neticket.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtUtil jwtUtil;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private TicketInfoRepository ticketInfoRepository;

  private SignupRequestDto signupRequestDto;
  private LoginRequestDto loginRequestDto;
  private User testUser;

  @BeforeEach
  public void setUp() {
    signupRequestDto = new SignupRequestDto("email@example.com", "nickname", "password");
    loginRequestDto = new LoginRequestDto("email@example.com", "password");
    testUser = new User(signupRequestDto, "encodedPassword");
  }

  @Test
  public void signup_success() {
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(userRepository.existsByNickname(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

    MessageResponseDto result = userService.signup(signupRequestDto);

    assertEquals(HttpStatus.CREATED.value(), result.getStatusCode());
    assertEquals("회원가입이 완료되었습니다.", result.getMsg());
  }

  @Test
  public void login_success() {
    HttpServletResponse response = mock(HttpServletResponse.class);

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(jwtUtil.createToken(anyString(), anyString(), any())).thenReturn("token");

    LoginResponseDto result = userService.login(loginRequestDto, response);

    assertEquals(testUser.getNickname(), result.getNickname());
    assertEquals(testUser.getRole().equals(UserRoleEnum.ADMIN), result.isAdmin());
  }

  @Test
  public void getUserInfo_success() {
    List<Reservation> reservations = new ArrayList<>();
    List<TicketInfo> ticketInfos = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      Reservation reservation = mock(Reservation.class);
      when(reservation.getTicketInfoId()).thenReturn((long) i + 1);
      reservations.add(reservation);

      TicketInfo ticketInfo = mock(TicketInfo.class);
      Event event = mock(Event.class);
      when(event.getImage()).thenReturn("image" + (i + 1));
      when(ticketInfo.getEvent()).thenReturn(event);
      ticketInfos.add(ticketInfo);
    }

    when(reservationRepository.findAllByUserOrderByIdDesc(any(User.class))).thenReturn(reservations);
    when(ticketInfoRepository.findById(anyLong())).thenReturn(Optional.of(ticketInfos.get(0)), Optional.of(ticketInfos.get(1)), Optional.of(ticketInfos.get(2)));

    List<ReservationResponseDto> result = userService.getUserInfo(testUser);

    assertEquals(3, result.size());
    assertEquals(reservations.stream().map(Reservation::getId).collect(Collectors.toList()), result.stream().map(ReservationResponseDto::getId).collect(Collectors.toList()));
  }


}
