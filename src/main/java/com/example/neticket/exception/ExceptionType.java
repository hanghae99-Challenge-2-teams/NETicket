package com.example.neticket.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {

  /* 500, INTERNAL_SERVER_ERROR */
  IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "입출력 예외가 발생하였습니다."),

  /* 404, NOT_FOUND */
  NOT_FOUND_EVENT_EXCEPTION(HttpStatus.NOT_FOUND, "조회하신 공연 정보를 찾을 수 없습니다."),
  NOT_FOUND_TICKET_INFO_EXCEPTION(HttpStatus.NOT_FOUND, "조회하신 티켓 정보를 찾을 수 없습니다."),
  NOT_FOUND_RESERVATION_EXCEPTION(HttpStatus.NOT_FOUND, "조회하신 예매 기록을 찾을 수 없습니다."),
  NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  NOT_FOUND_CACHE_EXCEPTION(HttpStatus.NOT_FOUND, "해당 캐시를 찾을 수 없습니다."),

  /* 401, UNAUTHORIZED */
  USER_UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "사용자의 권한이 없습니다."),
  TOKEN_VALIDATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

  /* 400, BAD_REQUEST */
  ZERO_WORD_EXCEPTION(HttpStatus.BAD_REQUEST, "키워드를 작성해주세요."),
  INVALID_PAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "조회 가능한 페이지를 입력해주세요."),
  NO_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "공연 이미지를 업로드해주세요."),
  IMAGE_SIZE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지의 사이즈가 최대 사이즈(5MB)를 초과합니다."),
  IMAGE_FORMAT_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지의 형식은 JPEG, JPG, PNG, GIF 중 하나여야 합니다."),
  RESERVATION_UNAVAILABLE_EXCEPTION(HttpStatus.BAD_REQUEST , "예매가 불가능한 공연입니다."),
  OUT_OF_TICKET_EXCEPTION(HttpStatus.BAD_REQUEST, "남은 좌석이 없습니다."),
  USER_RESERVATION_NOT_MATCHING_EXCEPTION(HttpStatus.BAD_REQUEST, "예매기록이 회원정보와 일치하지 않습니다."),
  EXISTED_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
  EXISTED_NICKNAME_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 닉네임입니다."),
  PASSWORD_NOT_MATCHING_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  EXISTED_CACHE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 Redis에 저장되어있습니다."),
  CANCEL_DEADLINE_PASSED_EXCEPTION(HttpStatus.BAD_REQUEST, "공연날짜가 지나 예매취소가 불가능합니다.");

  private final HttpStatus httpStatus;
  private final String message;

}
