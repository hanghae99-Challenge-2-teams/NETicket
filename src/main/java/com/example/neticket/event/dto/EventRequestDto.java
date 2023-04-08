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

}
