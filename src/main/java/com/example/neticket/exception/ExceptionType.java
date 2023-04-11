package com.example.neticket.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {

//  쓴 예외

  BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "입출력 예외가 발생하였습니다."),
  USER_UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "사용자의 권한이 없습니다."),
  NOT_FOUND_EVENT_EXCEPTION(HttpStatus.NOT_FOUND, "조회하신 공연 정보를 찾을 수 업습니다."),
  NOT_FOUND_TICKET_INFO_EXCEPTION(HttpStatus.NOT_FOUND, "조회하신 티켓 정보를 찾을 수 업습니다."),
  ZERO_WORD_EXCEPTION(HttpStatus.BAD_REQUEST, "키워드를 작성해주세요."),
  NO_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "공연 이미지를 업로드해주세요"),
  IMAGE_SIZE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지의 사이즈가 최대 사이즈(5MB)를 초과합니다."),
  IMAGE_FORMAT_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지의 형식은 JPEG, JPG, PNG, GIF 중 하나여야 합니다."),
  TOKEN_VALIDATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
  RESERVATION_UNAVAILABLE_EXCEPTION(HttpStatus.BAD_REQUEST , "예매가 불가능한 공연입니다."),
  OUT_OF_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "남은 좌석이 없습니다."),
  NOT_FOUND_RESERVATION_EXCEPTION(HttpStatus.NOT_FOUND, "조회하신 예매 기록을 찾을 수 업습니다."),
  USER_RESERVATION_NOT_MATCHING_EXCEPTION(HttpStatus.BAD_REQUEST, "예매기록이 회원정보와 일치하지 않습니다."),
  NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  EXISTED_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
  EXISTED_NICKNAME_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 닉네임입니다."),
  PASSWORD_NOT_MATCHING_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  //  안쓴 예외
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),
  ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, "로그인 후 사용해주세요.");


  private final HttpStatus httpStatus;
  private final String message;



}
