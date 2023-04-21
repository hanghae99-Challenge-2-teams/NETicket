package com.example.neticket.event.dto;

import com.example.neticket.event.entity.TicketInfo;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketInfoResponseDto {

  private Long ticketInfoId;
  private LocalDateTime openDate;
  private boolean isAvailable;

  // DetailEventResponseDto 역직렬화 할때 사용함
  @Builder
  public TicketInfoResponseDto() {
  }

  public TicketInfoResponseDto(TicketInfo ticketInfo) {
    this.ticketInfoId = ticketInfo.getId();
    this.openDate = ticketInfo.getOpenDate();
    this.isAvailable = ticketInfo.isAvailable();
  }
}
