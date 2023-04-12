package com.example.neticket.event.service;

import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketInfoService {

  private final TicketInfoRepository ticketInfoRepository;

  /**
   * 일단은 테스트를 위해서 매 분에 돌아간다!!!!!!!!!!!!!!
   * 3번째에있는 *를 0으로 수정하면 원래대로돌아감
   * 매일 자정(00시00분00초)에 event의 date(공연날짜)와 현재 날짜를 비교해
   * 오늘이 공연 당일(혹은 현재 날짜를 지나가면) 공연을 예매 불가로 설정한다.
   */

  @Scheduled(cron = "0 0 0 * * ?")
  public void updateTicketInfoAtMidnight() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
    LocalDate today = LocalDate.now();
    for (TicketInfo ticketInfo : ticketInfos) {
      LocalDate eventDate = ticketInfo.getEvent().getDate().toLocalDate();
      if (today.isEqual(eventDate) || today.isAfter(eventDate)) {
        ticketInfo.setAvailable(false);
      }
    }
    ticketInfoRepository.saveAll(ticketInfos);
  }

  /**
   * 일단은 테스트를 위해서 매 시간 돌아간다!!!!!!!!!!!!!!
   * 3번째에있는 *를 18로 수정하면 원래대로돌아감
   * 매일 18시 티켓정보의 openDate 오픈시간을 지나면 isAvailable을 true로 바꾼다.
   * 추후 수정이 필요하다. 정확히 00초가 아니고 딜레이가 생길 가능성이 있다.
   */

  @Scheduled(cron = "0 0 18 * * ?")
  public void updateTicketInfoAt6pm() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
    for (TicketInfo ticketInfo : ticketInfos) {
      LocalDateTime openDate = ticketInfo.getOpenDate();
      if (LocalDateTime.now().equals(openDate) || LocalDateTime.now().isAfter(openDate)) {
        ticketInfo.setAvailable(true);
      } else {
        ticketInfo.setAvailable(false);
      }
      ticketInfoRepository.saveAll(ticketInfos);
    }
  }




}
