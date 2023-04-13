package com.example.neticket.event.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EventRequestDto {

  private String title;
  private String place;
  private int price;
  private LocalDateTime date;
  private LocalDateTime openDate;
  private int totalSeat;

  public EventRequestDto(String title, String place, int price, LocalDateTime date,
      LocalDateTime openDate, int totalSeat) {
    this.title = title;
    this.place = place;
    this.price = price;
    this.date = date;
    this.openDate = openDate;
    this.totalSeat = totalSeat;
  }
}
