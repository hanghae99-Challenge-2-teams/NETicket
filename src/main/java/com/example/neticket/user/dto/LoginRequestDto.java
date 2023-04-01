package com.example.neticket.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequestDto {

  @NotBlank(message = "이메일을 입력해주세요.")
  @Pattern(regexp = "^(?=.{1,30}$)[A-Za-z0-9_.-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+$", message = "이메일 형식에 맞지 않습니다. 최대 30자 이내여야 합니다.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[a-zA-Z\\d!@#$%^&*()_+]{8,20}$", message = "8~20글자, 영문자 1개, 숫자 1개, 특수문자 1개 꼭 입력해야합니다.")
  private String password;

}
