package com.example.neticket.event.dto;

import com.example.neticket.event.entity.ShowTime;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ShowtimeResponseDto {

  private Long id;
  private LocalDate date;
  private int leftSeats;


  public ShowtimeResponseDto(ShowTime showTime) {
    this.id = showTime.getId();
    this.date = showTime.getDate();
    this.leftSeats = showTime.getTotalSeats() - showTime.getReservedSeats();
  }
}
