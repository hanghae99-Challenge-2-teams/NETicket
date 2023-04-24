package com.example.neticket.event.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.reservation.repository.RedisRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketInfoService {

  private final TicketInfoRepository ticketInfoRepository;
  private final RedisRepository redisRepository;
  private final RedisTemplate<String, DetailEventResponseDto> redisTemplate;

  /**
   * 매일 자정에 돌아가는 스케쥴러
   * 오늘이 공연일인 모든 공연을 예매 불가능으로 돌린다
   * repository에서 오늘이 공연 당일인 공연의 ticketInfo를 다 불러온다.
   * 해당 공연의 dto 캐시를 삭제한다
   * 해당 공연의 leftSeats 캐시도 삭제하고 DB에 정합성을 맞춘다.
   * ticketInfo에 변화가 생긴 것을 repository에 반영한다.
   */

  @Scheduled(cron = "0 0 0 * * ?")
  public void closeTicket() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findTicketInfoByEventDate();
    System.out.println(ticketInfos);
    for (TicketInfo ticketInfo : ticketInfos) {
      ticketInfo.setAvailable(false);
      deleteDtoCache(ticketInfo);
      redisRepository.deleteLeftSeatsInRedis(ticketInfo);
    }
    ticketInfoRepository.saveAll(ticketInfos);
  }

//  dto 캐시를 삭제하는 private 메서드
  private void deleteDtoCache(TicketInfo ticketInfo){
    String cacheKey = "DetailEventResponseDto::" + ticketInfo.getEvent().getId();
    Boolean eventCache = redisTemplate.hasKey(cacheKey);
    if (eventCache != null && eventCache) {
      redisTemplate.delete(cacheKey);
    }

  }

  /**
   * 매일 18시 티켓정보의 openDate가 오늘인 공연의 isAvailable을 true로 바꾼다.
   * 해당 ticketInfo의 leftSeats를 redis의 캐시로 저장한다.
   */

  @Scheduled(cron = "0 0 18 * * ?")
  public void openTicket() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findTicketInfoByOpenDate();
    System.out.println(ticketInfos);
    for (TicketInfo ticketInfo : ticketInfos) {
      deleteDtoCache(ticketInfo);
      ticketInfo.setAvailable(true);
      redisRepository.saveTicketInfoToRedis(ticketInfo);
    }
    ticketInfoRepository.saveAll(ticketInfos);
  }


//  admin 유저가 새로 서버 시작할때 ticketInfo 업데이트
  public MessageResponseDto resetTicketInfo() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
    redisRepository.flushAll();
    LocalDateTime today = LocalDateTime.now();
    for (TicketInfo ticketInfo : ticketInfos) {
      LocalDateTime openDate = ticketInfo.getOpenDate();
      LocalDate eventDate = ticketInfo.getEvent().getDate().toLocalDate();
//      티켓 오픈 시간이 오늘보다 뒤 일때 false
      if (today.isBefore(openDate)){
        ticketInfo.setAvailable(false);
//        티켓 오픈시간이 오늘~공연당일 일때 true
      } else if ((today.isAfter(openDate) || today.isEqual(openDate)) && LocalDate.now().isBefore(eventDate)){
        ticketInfo.setAvailable(true);
        redisRepository.saveTicketInfoToRedis(ticketInfo);
//        오늘이 공연 당일이거나 이미 지난 공연일 때
      } else {
        ticketInfo.setAvailable(false);
      }
    }
    ticketInfoRepository.saveAll(ticketInfos);
    return new MessageResponseDto(HttpStatus.OK, "공연 상태를 성공적으로 조정했습니다.");
  }




}
