package com.example.neticket.user.entity;

import com.example.neticket.user.dto.SignupRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 30)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true, length = 10)
  private String nickname;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role = UserRoleEnum.USER;

  public User(SignupRequestDto dto, String password) {
    this.email = dto.getEmail();
    this.password = password;
    this.nickname = dto.getNickname();
  }

}
