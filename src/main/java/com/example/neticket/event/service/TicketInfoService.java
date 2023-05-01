package com.example.neticket.event.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.reservation.repository.RedisRepository;
import com.example.neticket.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketInfoService {

  private final TicketInfoRepository ticketInfoRepository;
  private final RedisRepository redisRepository;
  private final ReservationRepository reservationRepository;
  private final RedisTemplate<String, DetailEventResponseDto> redisTemplate;

  /**
   * 매일 자정에 돌아가는 스케쥴러 오늘이 공연일인 모든 공연을 예매 불가능으로 돌린다.
   * repository에서 오늘이 공연 당일인 공연의 ticketInfo를 다 불러온다.
   * 해당 공연의 dto 캐시를 삭제한다. 해당 공연의 leftSeats 캐시도 삭제하고 DB에 정합성을 맞춘다.
   * ticketInfo에 변화가 생긴 것을 repository에 반영한다.
   */

  @Scheduled(cron = "0 0 0 * * ?")
  public void closeTicket() {
    List<TicketInfo> ticketInfos = ticketInfoRepository.findTicketInfoByEventDate();
    for (TicketInfo ticketInfo : ticketInfos) {
      ticketInfo.setAvailable(false);
      deleteDtoCache(ticketInfo);
      redisRepository.deleteLeftSeatsInRedis(ticketInfo);
      log.info("{}번 공연이 예매불가능으로 수정되었습니다.", ticketInfo.getEvent().getId());
    }
    ticketInfoRepository.saveAll(ticketInfos);

  }

  // dto 캐시를 삭제하는 private 메서드
  private void deleteDtoCache(TicketInfo ticketInfo) {
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
    for (TicketInfo ticketInfo : ticketInfos) {
      deleteDtoCache(ticketInfo);
      ticketInfo.setAvailable(true);
      redisRepository.saveTicketInfoToRedis(ticketInfo);
      log.info("{}번 공연이 예매가능으로 수정되었습니다.", ticketInfo.getEvent().getId());
    }
    ticketInfoRepository.saveAll(ticketInfos);
  }


  // admin 유저가 새로 서버 시작할때 ticketInfo 업데이트
  @Transactional
  public MessageResponseDto resetTicketInfo() {
    redisRepository.flushAll();
    reservationRepository.deleteAll();
    ticketInfoRepository.updateLeftSeatsToTotalSeats();
    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
    LocalDateTime today = LocalDateTime.now();
    for (TicketInfo ticketInfo : ticketInfos) {
      LocalDateTime openDate = ticketInfo.getOpenDate();
      LocalDate eventDate = ticketInfo.getEvent().getDate().toLocalDate();
      // 티켓 오픈 시간인 공연만 isAvalilable을 true로 수정
      if ((today.isAfter(openDate) || today.isEqual(openDate)) && LocalDate.now()
          .isBefore(eventDate)) {
        ticketInfo.setAvailable(true);
        redisRepository.saveTicketInfoToRedis(ticketInfo);
        // 지나간 공연이거나 미오픈 공연만 isAvalilable을 false로 수정
      } else {
        ticketInfo.setAvailable(false);
      }
    }
    ticketInfoRepository.saveAll(ticketInfos);
    return new MessageResponseDto(HttpStatus.OK, "공연 상태를 성공적으로 조정했습니다.");
  }

}
