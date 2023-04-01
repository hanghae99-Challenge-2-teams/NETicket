package com.example.neticket.user.service;


import com.example.neticket.jwt.JwtUtil;
import com.example.neticket.user.dto.LoginRequestDto;
import com.example.neticket.user.dto.SignupRequestDto;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.entity.UserRoleEnum;
import com.example.neticket.user.repository.UserRepository;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  // 회원가입
  @Transactional
  public void signup(SignupRequestDto dto) {

    boolean isEmailExist = userRepository.existsByEmail(dto.getEmail());
    if (isEmailExist) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    boolean isNickExist = userRepository.existsByNickname(dto.getNickname());
    if (isNickExist) {
      throw new IllegalArgumentException("이미 가입된 닉네임입니다.");
    }

    String password = passwordEncoder.encode(dto.getPassword());

    UserRoleEnum role = UserRoleEnum.USER;

    if(dto.isAdmin()){ // is는 boolean 값을 반전
      if(!dto.getAdminToken().equals(UserRoleEnum.Authority.ADMIN)){
        throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능 합니다.");
      }
      role = UserRoleEnum.ADMIN;
    }

    User user = new User(dto ,password, role);
    userRepository.saveAndFlush(user);

  }

  // 로그인
  @Transactional
  public void login(LoginRequestDto dto, HttpServletResponse response) {

    User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
        () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
    );

    String encodePassword = user.getPassword();

    // 비밀번호 확인
    if(!passwordEncoder.matches(dto.getPassword(), encodePassword)){
      throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getEmail(), user.getRole()));
    // Cookie 생성 및 직접 브라우저에 Set 위와 아래 방법중 택일
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getEmail(), user.getRole());
////        cookie.setPath("/");
//        response.addCookie(cookie);
  }

}

