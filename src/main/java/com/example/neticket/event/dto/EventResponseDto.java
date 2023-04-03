package com.example.neticket.event.dto;

import com.example.neticket.event.entity.Event;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class EventResponseDto {

  private Long id;
  private String title;
  private LocalDate date;
  private String place;
  private String image;

  public EventResponseDto(Event event) {
    this.id = event.getId();
    this.title = event.getTitle();
    this.place = event.getPlace();
    this.image = event.getImage();
    this.date = event.getDate();
  }
}
