package com.example.neticket.event.dto;

import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.ShowTime;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class EventResponseDto {

  private Long id;
  private String title;

//  이거 리스트로?
  private String date;
  private String place;
  private String image;

  public EventResponseDto(Event event) {
    this.id = event.getId();
    this.title = event.getTitle();
    this.place = event.getPlace();
    this.image = event.getImage();
//    date/date/date 이런 양식으로 프론트에 전달
    List<ShowTime> showTimeList = event.getShowTimeList();
    StringBuilder dates = new StringBuilder(); // stringbuilder 사용
    for (ShowTime showTime : showTimeList) {
      dates.append(showTime.getDate().toString()).append("/");
    }
    if (dates.length() > 0) {
      dates.setLength(dates.length() - 1); // 마지막 슬래시 제거
    }
    this.date = dates.toString();

  }
}
