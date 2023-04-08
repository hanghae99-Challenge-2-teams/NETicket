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
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TicketInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(nullable = false)
  private boolean isAvailable = false;

  @Column(nullable = false)
  private int totalSeats;

  @Column(nullable = false)
  private int leftSeats;

  @Column(nullable = false)
  private LocalDateTime openDate;

//  공연회차-티켓예매정보  양방향 일대일 관계로 설정
  @OneToOne
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  public TicketInfo(EventRequestDto eventRequestDto, Event event) {
    this.openDate = eventRequestDto.getOpenDate();
    this.totalSeats = eventRequestDto.getTotalSeat();
    this.leftSeats = eventRequestDto.getTotalSeat();
    this.event = event;
  }

  public int reserveSeats(int count) {
    return this.leftSeats -= count;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

}
