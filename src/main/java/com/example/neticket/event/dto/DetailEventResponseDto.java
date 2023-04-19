package com.example.neticket.event.dto;

import com.example.neticket.event.entity.Event;
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
