package com.example.neticket.event.dto;

import com.example.neticket.event.entity.Event;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class DetailEventResponseDto {

  private Long eventId;
  private String title;
  private String image;
  private String place;
  private LocalDateTime date;
  private int price;
  private TicketInfoResponseDto ticketInfoDto;

  public DetailEventResponseDto(Event event) {
    this.eventId = event.getId();
    this.title = event.getTitle();
    this.image = event.getImage();
    this.place = event.getPlace();
    this.date = event.getDate();
    this.price = event.getPrice();
    this.ticketInfoDto = new TicketInfoResponseDto(event.getTicketInfo());
  }
}
