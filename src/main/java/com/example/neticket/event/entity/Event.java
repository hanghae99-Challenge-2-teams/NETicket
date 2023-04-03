package com.example.neticket.event.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private String place;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private LocalDate date;

//  공연정보와 티켓예매정보를 1대1 매칭
  @OneToOne(mappedBy = "event")
  private TicketInfo ticketInfo;

}
