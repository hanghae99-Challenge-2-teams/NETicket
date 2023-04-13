package com.example.neticket.reservation.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationRequestDto {

  @NotNull(message = "티켓 정보가 없습니다.")
  private Long ticketInfoId;

  @Min(1)
  @Max(4)
  private int count;

  public ReservationRequestDto(Long ticketInfoId, int count) {
    this.ticketInfoId = ticketInfoId;
    this.count = count;
  }
}
