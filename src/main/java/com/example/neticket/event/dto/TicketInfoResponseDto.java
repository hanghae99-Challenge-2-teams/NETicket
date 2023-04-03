package com.example.neticket.event.dto;

import com.example.neticket.event.entity.TicketInfo;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TicketInfoResponseDto {

  private Long ticketInfoId;
  private LocalDateTime openDate;
  private int leftSeats;
  private boolean isAvailable;


  public TicketInfoResponseDto(TicketInfo ticketInfo) {
    this.ticketInfoId = ticketInfo.getId();
    this.openDate = ticketInfo.getOpenDate();
    this.leftSeats = ticketInfo.getTotalSeats() - ticketInfo.getReservedSeats();
    this.isAvailable = ticketInfo.isAvailable();
  }
}
