package com.example.neticket.reservation.dto;

import com.example.neticket.reservation.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponseDto {

    private Long id;
    private String image;
    private String title;
    private String place;
    private LocalDateTime date;
    private int totalPrice;
    private int count;

    public ReservationResponseDto(Reservation reservation) {
      this.id = reservation.getId();
      this.image = reservation.getTicketInfo().getEvent().getImage();
      this.title = reservation.getTicketInfo().getEvent().getTitle();
      this.place = reservation.getTicketInfo().getEvent().getPlace();
      this.date = reservation.getTicketInfo().getEvent().getDate();
      this.count = reservation.getCount();
      this.totalPrice = reservation.getTicketInfo().getEvent().getPrice() * this.count;
    }

}
