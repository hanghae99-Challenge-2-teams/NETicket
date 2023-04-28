package com.example.neticket.event.entity;

import com.example.neticket.event.dto.EventRequestDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TicketInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private boolean isAvailable = false;

  @Column(nullable = false)
  private int totalSeats;

  @Column(nullable = false)
  private int leftSeats;

  @Column(nullable = false)
  private LocalDateTime openDate;

  // 공연회차-티켓예매정보  양방향 1대1 관계 설정
  // ticketInfo가 연관관계의 주인
  @OneToOne
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  public TicketInfo(EventRequestDto eventRequestDto, Event event) {
    this.openDate = eventRequestDto.getOpenDate();
    this.totalSeats = eventRequestDto.getTotalSeat();
    this.leftSeats = eventRequestDto.getTotalSeat();
    this.event = event;
    event.setTicketInfo(this);
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  public int minusSeats(int count) {
    return this.leftSeats -= count;
  }

  public void plusSeats(int count) {
    this.leftSeats += count;
  }

  public void setLeftSeats(int leftSeats) {
    this.leftSeats = leftSeats;
  }

}
