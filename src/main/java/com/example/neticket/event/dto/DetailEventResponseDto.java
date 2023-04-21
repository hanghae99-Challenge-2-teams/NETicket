package com.example.neticket.event.dto;

import com.example.neticket.event.entity.Event;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailEventResponseDto {

  private Long eventId;
  private String title;
  private String image;
  private String place;
  private LocalDateTime date;
  private int price;
  private TicketInfoResponseDto ticketInfoDto;

  // DetailEventResponseDto 역직렬화 할때 사용함
  @Builder
  public DetailEventResponseDto() {
  }

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
