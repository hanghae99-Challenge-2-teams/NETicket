package com.example.neticket.event.dto;

import com.example.neticket.event.entity.Event;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class DetailEventResponseDto {

  private String title;
  private String image;
  private String place;
  private int price;
  private List<ShowtimeResponseDto> showTimeList;

  public DetailEventResponseDto(Event event) {
    this.title = event.getTitle();
    this.image = event.getImage();
    this.place = event.getPlace();
    this.price = event.getPrice();
    this.showTimeList = event.getShowTimeList().stream()
        .map(ShowtimeResponseDto::new)
        .collect(Collectors.toList());
  }
}
