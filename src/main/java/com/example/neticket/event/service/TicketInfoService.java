package com.example.neticket.event.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.reservation.repository.RedisRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketInfoService {

  private final TicketInfoRepository ticketInfoRepository;
  private final RedisRepository redisRepository;
  private final RedisTemplate<String, DetailEventResponseDto> redisTemplate;

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
        // ticketInfo 에 있는 데이터를 수정하고 캐시를 삭제 하려고한다.
        // 삭제하려고 하는 키를 생성
        String cacheKey = "DetailEventResponseDto::" + ticketInfo.getEvent().getId();
        // 삭제하려고 하는 캐시를 찾는다.
        //  반환값이 true인 경우 cacheKey에 해당하는 키가 Redis에 존재하고,
        //  false인 경우 cacheKey에 해당하는 키가 Redis에 존재하지 않습니다.
        boolean eventCache = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
        if (eventCache) {
          redisTemplate.delete(cacheKey);
        }
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

//  @Scheduled(cron = "0 0 18 * * ?")
//  public void updateTicketInfoAt6pm() {
//    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
//    for (TicketInfo ticketInfo : ticketInfos) {
//      LocalDateTime openDate = ticketInfo.getOpenDate();
//      if (LocalDateTime.now().equals(openDate) || LocalDateTime.now().isAfter(openDate)) {
//        ticketInfo.setAvailable(true);
//      } else {
//        ticketInfo.setAvailable(false);
//      }
//      ticketInfoRepository.saveAll(ticketInfos);
//    }
//  }

  @Scheduled(cron = "0 0 18 * * *")
  public void updateTicketInfoAt6pm() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
    for (TicketInfo ticketInfo : ticketInfos) {
      LocalDate openDate = ticketInfo.getOpenDate().toLocalDate();
      if (LocalDate.now().equals(openDate)) {
        ticketInfo.setAvailable(true);
        redisRepository.saveTicketInfoToRedis(ticketInfo);
      }
      ticketInfoRepository.saveAll(ticketInfos);
    }
  }




}
