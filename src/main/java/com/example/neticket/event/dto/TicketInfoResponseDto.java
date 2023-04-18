package com.example.neticket.event.dto;

import com.example.neticket.event.entity.TicketInfo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketInfoResponseDto {

  private Long ticketInfoId;
  private LocalDateTime openDate;
//  private int leftSeats;
  private boolean isAvailable;


  public TicketInfoResponseDto(TicketInfo ticketInfo) {
    this.ticketInfoId = ticketInfo.getId();
    this.openDate = ticketInfo.getOpenDate();
//    this.leftSeats = ticketInfo.getLeftSeats();
    this.isAvailable = ticketInfo.isAvailable();
  }
}
