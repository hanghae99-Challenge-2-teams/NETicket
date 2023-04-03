package com.example.neticket.reservation.dto;

import lombok.Getter;

@Getter
public class ReservationRequestDto {

  private Long ticketInfoId;

// 추후 최대 예매 자리수에 대해서 의논 정해봐야함
  private int count;

}
