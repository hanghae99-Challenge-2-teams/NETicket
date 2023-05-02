package com.example.neticket.security;

import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;


  //  토큰에 있는 email로 유저 정보 조회
  @Override
//  @Cacheable(cacheNames = "localCache", key = "#email")
  public UserDetails loadUserByUsername(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_USER_EXCEPTION)
    );
    return new UserDetailsImpl(user, user.getEmail());

  }

}
