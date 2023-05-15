package com.example.neticket.reservation.repository;

import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRepository {

  private final RedisTemplate<String, Integer> redisTemplate;
  private final TicketInfoRepository ticketInfoRepository;
  private final ReservationRepository reservationRepository;
  private final RedisScript<Boolean> decrementLeftSeatRedisScript = new DefaultRedisScript<>(
      DECREMENT_LEFT_SEAT_SCRIPT, Boolean.class);

  // Lua Script
  private static final String DECREMENT_LEFT_SEAT_SCRIPT =
      "local leftSeats = tonumber(redis.call('get', KEYS[1])) " +
          "if leftSeats - ARGV[1] >= 0 then " +
          "  redis.call('decrby', KEYS[1], ARGV[1]) " +
          "  return true " +
          "else " +
          "  return false " +
          "end";

  // key를 "ls+ticketInfoId"로 저장. ls는 소문자. key가 존재하면 생략
  public void saveTicketInfoToRedis(TicketInfo ticketInfo) {
    String key = "ls" + ticketInfo.getId();
    if (hasLeftSeatsInRedis(ticketInfo.getId())) {
      return;
    }
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime eventDate = ticketInfo.getEvent().getDate();
    redisTemplate.opsForValue()
        .set(key, ticketInfo.getLeftSeats(), Duration.between(today, eventDate));
  }

  // 1분 마다 캐시 수정부분을 DB에 저장
  // Write Back 캐시 쓰기 전략 적용
  @Scheduled(cron = "0 * * * * *")
  public void saveTicketInfoFromRedis() {
    Set<String> keys = redisTemplate.keys("ls*");
    if (keys == null || keys.isEmpty()) {
      return;
    }
    for (String key : keys) {
      Integer leftSeatsInRedis = redisTemplate.opsForValue().get(key);
      Long ticketInfoId = Long.parseLong(key.substring(2));
      TicketInfo ticketInfo = ticketInfoRepository.findById(ticketInfoId).orElseThrow(
          () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
      ticketInfo.setLeftSeats(leftSeatsInRedis);
      ticketInfoRepository.save(ticketInfo);
    }

  }

  // key로 캐시 존재 여부 확인
  public Boolean hasLeftSeatsInRedis(Long ticketInfoId) {
    String key = "ls" + ticketInfoId;
    try {
      return redisTemplate.hasKey(key);
    } catch (Exception e) {
      return false;
    }
  }

  // count만큼 남은좌석수 차감
  public Boolean decrementLeftSeatInRedis(Long ticketInfoId, int count) {
    String key = "ls" + ticketInfoId;
    // Lua Script 실행
    return redisTemplate.execute(decrementLeftSeatRedisScript, Collections.singletonList(key),
        count);
  }

  // count만큼 남은좌석수 추가. 예매취소에 사용
  public void incrementLeftSeatInRedis(Long ticketInfoId, int count) {
    String key = "ls" + ticketInfoId;
    redisTemplate.opsForValue().increment(key, count);
  }

  // Redis에서 남은좌석수 수동삭제. 삭제 후 Refresh적용
  public void deleteLeftSeatsInRedis(TicketInfo ticketInfo) {
    String key = "ls" + ticketInfo.getId();
    if (hasLeftSeatsInRedis(ticketInfo.getId())) {
      redisTemplate.delete(key);
      refreshLeftSeats(ticketInfo);
    }
  }

  // redis 캐시 전체 삭제
  public void flushAll() {
    redisTemplate.execute((RedisConnection connection) -> {
      connection.flushAll();
      return "OK";
    });
  }

  // 현재 Redis에 등록된 key 목록 반환
  public Set<String> findAllLeftSeatsKeysInRedis() {
    return redisTemplate.keys("ls*");
  }

  // Redis와 DB에 들어있는 LeftSeat의 데이터 정합성 문제 해결
  public void refreshLeftSeats(TicketInfo ticketInfo) {
    Integer accurateReservedSeats = reservationRepository.sumCountByTicketInfoId(
        ticketInfo.getId());
    if (accurateReservedSeats == null) {
      accurateReservedSeats = 0;
    }
    int accurateLeftSeats = ticketInfo.getTotalSeats() - accurateReservedSeats;
    ticketInfo.setLeftSeats(accurateLeftSeats);
    if (hasLeftSeatsInRedis(ticketInfo.getId())) {
      String key = "ls" + ticketInfo.getId();
      redisTemplate.opsForValue().set(key, accurateLeftSeats);
    }
  }

}
