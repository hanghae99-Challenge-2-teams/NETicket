//package com.example.neticket.event.service;
//
//import com.example.neticket.event.entity.TicketInfo;
//import com.example.neticket.event.repository.TicketInfoRepository;
//import com.example.neticket.reservation.repository.RedisRepository;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CacheWarmingService {
//  private final RedisRepository redisRepository;
//  private final TicketInfoRepository ticketInfoRepository;
//
//  private final Logger logger = LoggerFactory.getLogger(CacheWarmingService.class);
//
//  @Scheduled(cron = "0 * * * * *") // 매일 0시에 캐시 워밍 실행
//  public void warmUpCacheForOneWeekBeforeEvents() {
//    LocalDateTime oneWeekFromNow = LocalDateTime.now().plusWeeks(1);
//
//    List<TicketInfo> ticketInfos = ticketInfoRepository.findAll();
//    for (TicketInfo ticketInfo : ticketInfos) {
//      LocalDateTime eventDateTime = ticketInfo.getOpenDate();
//      LocalDate eventDate = eventDateTime.toLocalDate();
//      if (eventDate.isEqual(oneWeekFromNow.toLocalDate())) {
//        redisRepository.saveTicketInfoToRedis(ticketInfo);
//        logger.info("Cache warmed for TicketInfoId: {}", ticketInfo.getId());
//      }
//    }
//    logger.info("Cache warming completed.");
//  }
//}
//
//
