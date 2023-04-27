package com.example.neticket.user.dto;

import com.example.neticket.user.entity.User;
import com.example.neticket.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LoginResponseDto {

  private String nickname;
  private boolean isAdmin = false;

  public LoginResponseDto(User user) {
    this.nickname = user.getNickname();
    if (user.getRole().equals(UserRoleEnum.ADMIN)) {
      this.isAdmin = true;
    }
  }
}
