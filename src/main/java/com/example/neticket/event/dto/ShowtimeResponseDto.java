package com.example.neticket.event.dto;

import com.example.neticket.event.entity.ShowTime;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ShowtimeResponseDto {

  private LocalDate date;
  private int leftSeats;


  public ShowtimeResponseDto(ShowTime showTime) {
    this.date = showTime.getDate();
    this.leftSeats = showTime.getTotalSeats() - showTime.getReservedSeats();
  }
}
