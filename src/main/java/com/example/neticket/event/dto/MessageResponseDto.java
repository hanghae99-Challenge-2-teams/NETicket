package com.example.neticket.event.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageResponseDto {
    private int statusCode;
    private String msg;

    public MessageResponseDto(HttpStatus httpStatus, String msg) {
        this.statusCode = httpStatus.value();
        this.msg = msg;
    }

}
