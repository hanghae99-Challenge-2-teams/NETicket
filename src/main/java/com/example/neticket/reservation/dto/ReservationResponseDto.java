package com.example.neticket.reservation.dto;

import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.reservation.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ReservationResponseDto {

    private Long id;
    private String image;
    private String title;
    private String place;
    private LocalDateTime date;
    private int totalPrice;
    private int count;

    public ReservationResponseDto(Reservation reservation, TicketInfo ticketInfo) {
      this.id = reservation.getId();
      this.image = ticketInfo.getEvent().getImage();
      this.title = ticketInfo.getEvent().getTitle();
      this.place = ticketInfo.getEvent().getPlace();
      this.date = ticketInfo.getEvent().getDate();
      this.count = reservation.getCount();
      this.totalPrice = ticketInfo.getEvent().getPrice() * this.count;
    }

}
