package com.example.neticket.event.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false, length = 30)
  private String place;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private LocalDateTime date;

//  공연정보와 티켓예매정보를 1대1 매칭
  @OneToOne(mappedBy = "event")
  private TicketInfo ticketInfo;

}
