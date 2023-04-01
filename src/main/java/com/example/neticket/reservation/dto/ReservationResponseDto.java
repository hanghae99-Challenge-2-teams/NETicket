package com.example.neticket.reservation.dto;

import com.example.neticket.reservation.entity.Reservation;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponseDto {

    private Long id;
    private String image;
    private String title;
    private String place;
    private LocalDate date;
    private int totalPrice;
    private int count;

    public ReservationResponseDto(Reservation reservation) {
      this.id = reservation.getId();
      this.image = reservation.getShowTime().getEvent().getImage();
      this.title = reservation.getShowTime().getEvent().getTitle();
      this.place = reservation.getShowTime().getEvent().getPlace();
      this.date = reservation.getShowTime().getDate();
      this.count = reservation.getCount();
      this.totalPrice = reservation.getShowTime().getEvent().getPrice() * this.count;
    }

}
