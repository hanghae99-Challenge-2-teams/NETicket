package com.example.neticket.reservation.repository;

import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRepository {
  private final RedisTemplate<String, Integer> redisTemplate;
  private final TicketInfoRepository ticketInfoRepository;
  private final ReservationRepository reservationRepository;

//  키를 ls1 ls2 이런 패턴으로 "ls+ticketInfoId"로 저장. ls는 소문자. 키가 이미 존재하면 예외처리
  public void saveTicketInfoToRedis(TicketInfo ticketInfo) {
    String key = "ls" + ticketInfo.getId();
    if (redisTemplate.hasKey(key)) {
      throw new CustomException(ExceptionType.EXISTED_CACHE_EXCEPTION);
    }
    redisTemplate.opsForValue().set(key, ticketInfo.getLeftSeats());
  }

//  매분 캐시 변경분을 db에 저장
  @Scheduled(cron = "0 * * * * *")
  public void saveTicketInfoFromRedis(){
    Set<String> keys = redisTemplate.keys("ls*");
    if (keys.isEmpty() || keys == null) return;
    for (String key : keys) {
      Integer leftSeatsInRedis = redisTemplate.opsForValue().get(key);
      Long ticketInfoId = Long.parseLong(key.substring(2));
      TicketInfo ticketInfo = ticketInfoRepository.findById(ticketInfoId).orElseThrow(
          () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
      ticketInfo.setLeftSeats(leftSeatsInRedis);
      ticketInfoRepository.save(ticketInfo);
    }

  }

//  key로 redis에 조회
  public Integer findLeftSeatsFromRedis(Long ticketInfoId){
    String key = "ls" + ticketInfoId;
    return redisTemplate.opsForValue().get(key);
  }

//  값 변경. count만큼 남은좌석수 차감
  public void decrementLeftSeatInRedis(Long ticketInfoId, int count){
    String key = "ls" + ticketInfoId;
    redisTemplate.opsForValue().decrement(key, count);
  }

  //  값 변경. count만큼 남은좌석수 추가. 예매취소에 사용
  public void incrementLeftSeatInRedis(Long ticketInfoId, int count){
    String key = "ls" + ticketInfoId;
    redisTemplate.opsForValue().increment(key, count);
  }

//  키 삭제. 삭제전 leftSeats 반영. 캐시가 없으면 예외처리
  public void deleteLeftSeatsInRedis(Long ticketInfoId){
    String key = "ls" + ticketInfoId;
    if (!redisTemplate.hasKey(key)) {
      throw new CustomException(ExceptionType.NOT_FOUND_CACHE_EXCEPTION);
    }
    saveTicketInfoFromRedis();
    redisTemplate.delete(key);
  }

//  현재 Redis에 등록된 key 목록 반환
  public Set<String> findAllLeftSeatsKeysInRedis() {
    return redisTemplate.keys("ls*");
  }

//
  public void refreshLeftSeats(Long ticketInfoId) {
    TicketInfo ticketInfo = ticketInfoRepository.findById(ticketInfoId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
    );
    Integer accurateReservedSeats = reservationRepository.sumCountByTicketInfoId(ticketInfoId);
    if (accurateReservedSeats == null) {
      throw new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION);
    }
    int accurateLeftSeats = ticketInfo.getTotalSeats() - accurateReservedSeats;
    String key = "ls" + ticketInfoId;
    ticketInfo.setLeftSeats(accurateLeftSeats);
//    덮어 쓰기
    redisTemplate.opsForValue().set(key, accurateLeftSeats);
  }
}
