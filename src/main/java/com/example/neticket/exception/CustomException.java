package com.example.neticket.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final HttpStatus httpStatus;

  public CustomException(ExceptionType exceptionType) {
    super(exceptionType.getMessage());
    this.httpStatus = exceptionType.getHttpStatus();
  }
}
