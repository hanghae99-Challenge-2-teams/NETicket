package com.example.neticket.event.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventRequestDto {

  @NotBlank
  private String title;

  @NotBlank
  private String place;

  @NotNull
  private int price;

  @NotNull
  private LocalDateTime date;

  @NotNull
  private LocalDateTime openDate;

  @NotNull
  private int totalSeat;

  // unitTest 생성자
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
