package com.example.neticket.reservation.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
