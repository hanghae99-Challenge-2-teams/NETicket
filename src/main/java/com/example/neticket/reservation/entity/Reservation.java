package com.example.neticket.reservation.entity;

import com.example.neticket.reservation.dto.ReservationRequestDto;
import com.example.neticket.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Reservation{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int count;

  // 예매-사용자 단방향 N:1 관계
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private Long ticketInfoId;

  public Reservation(ReservationRequestDto dto, User user) {
    this.count = dto.getCount();
    this.user = user;
    this.ticketInfoId = dto.getTicketInfoId();
  }

}
