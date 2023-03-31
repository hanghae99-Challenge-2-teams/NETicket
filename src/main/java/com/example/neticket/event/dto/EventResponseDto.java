package com.example.neticket.event.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class EventResponseDto {

  private Long id;
  private String title;

//  이거 리스트로?
  private LocalDate date;
  private String place;
  private String image;

}
