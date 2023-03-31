package com.example.neticket.reservation.entity;

import com.example.neticket.event.entity.ShowTime;
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
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int count;

//  예매-사용자 단방향 다대일 관계로 설정
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @Column(nullable = false)
  private User user;

//  예매-공연회차 단방향 다대일 관계로 설정
  @ManyToOne
  @JoinColumn(name = "show_time_id", nullable = false)
  @Column(nullable = false)
  private ShowTime showTime;


}
