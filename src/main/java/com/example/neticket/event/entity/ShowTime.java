package com.example.neticket.event.entity;

import java.time.LocalDate;
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
public class ShowTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private boolean isAvailable;

  @Column(nullable = false)
  private int totalSeats;

  @Column(nullable = false)
  private int reservedSeats;

//  공연회차-공연정보  양방향 다대일 관계로 설정
  @ManyToOne
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  public void reserveSeats(int count) {
    this.reservedSeats += count;
  }

}
