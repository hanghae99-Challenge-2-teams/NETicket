package com.example.neticket.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

  @NotBlank(message = "이메일을 입력해주세요.")
  @Pattern(regexp = "^(?=.{1,30}$)[A-Za-z0-9_.-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+$", message = "이메일 형식에 맞지 않습니다. 최대 30자 이내여야 합니다.")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[a-zA-Z\\d!@#$%^&*()_+]{8,20}$", message = "8~20글자, 영문자 1개, 숫자 1개, 특수문자 1개 꼭 입력해야합니다.")
  private String password;

  @NotBlank(message = "닉네임을 입력해주세요.")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 2 ~10자 한글,영어만 가능합니다.")
  private String nickname;

  private boolean admin = false; // is는 반전

  private String adminToken = ""; // 토큰 초기값으로 만든다.

}
