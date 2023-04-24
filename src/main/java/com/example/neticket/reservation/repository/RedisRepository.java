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
  private static final String DECREMENT_LEFT_SEAT_SCRIPT =
      "local leftSeats = tonumber(redis.call('get', KEYS[1])) " +
      "if leftSeats - ARGV[1] >= 0 then " +
      "  redis.call('decrby', KEYS[1], ARGV[1]) " +
      "  return true " +
      "else " +
      "  return false " +
      "end";

  private final RedisScript<Boolean> decrementLeftSeatRedisScript = new DefaultRedisScript<>(DECREMENT_LEFT_SEAT_SCRIPT, Boolean.class);


  //  키를 ls1 ls2 이런 패턴으로 "ls+ticketInfoId"로 저장. ls는 소문자. 키가 이미 존재하면 예외처리
  public void saveTicketInfoToRedis(TicketInfo ticketInfo) {
    String key = "ls" + ticketInfo.getId();
    if (hasLeftSeatsInRedis(ticketInfo.getId())) {
//      throw new CustomException(ExceptionType.EXISTED_CACHE_EXCEPTION);
      return;
    }
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime eventDate = ticketInfo.getEvent().getDate();
    redisTemplate.opsForValue().set(key, ticketInfo.getLeftSeats(), Duration.between(today,eventDate));
  }

//  매분 캐시 변경분을 db에 저장
  @Scheduled(cron = "0 * * * * *")
  public void saveTicketInfoFromRedis(){
    Set<String> keys = redisTemplate.keys("ls*");
    if (keys == null || keys.isEmpty()) return;
    for (String key : keys) {
      Integer leftSeatsInRedis = redisTemplate.opsForValue().get(key);
      Long ticketInfoId = Long.parseLong(key.substring(2));
      TicketInfo ticketInfo = ticketInfoRepository.findById(ticketInfoId).orElseThrow(
          () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION));
      ticketInfo.setLeftSeats(leftSeatsInRedis);
      ticketInfoRepository.save(ticketInfo);
    }

  }

//  key로 redis에 캐시가 있는지 조회하고 Boolean 반환
  public Boolean hasLeftSeatsInRedis(Long ticketInfoId){
    String key = "ls" + ticketInfoId;
    try{
      return redisTemplate.hasKey(key);
    } catch (Exception e) {
      return false;
    }
  }

//  값 변경. count만큼 남은좌석수 차감
  public Boolean decrementLeftSeatInRedis(Long ticketInfoId, int count){
    String key = "ls" + ticketInfoId;
//    Lua Script 실행
    return redisTemplate.execute(decrementLeftSeatRedisScript, Collections.singletonList(key), count);
  }

  //  값 변경. count만큼 남은좌석수 추가. 예매취소에 사용
  public void incrementLeftSeatInRedis(Long ticketInfoId, int count){
    String key = "ls" + ticketInfoId;
    redisTemplate.opsForValue().increment(key, count);
  }

//  키 삭제. 삭제전 leftSeats 반영. 캐시가 없으면 예외처리. 수동 삭제
  public void deleteLeftSeatsInRedis(TicketInfo ticketInfo){
    String key = "ls" + ticketInfo.getId();
    if (hasLeftSeatsInRedis(ticketInfo.getId())) {
      redisTemplate.delete(key);
      refreshLeftSeats(ticketInfo);
    }
  }

//  redis 캐시 flush
  public void flushAll(){
    redisTemplate.execute((RedisConnection connection) -> {
      connection.flushAll();
      return "OK";
    });
  }


//  현재 Redis에 등록된 key 목록 반환
  public Set<String> findAllLeftSeatsKeysInRedis() {
    return redisTemplate.keys("ls*");
  }

// Redis와 DB에 들어있는 LeftSeat의 정합성을 맞추기 위해 Reservation에서 ticketInfoId로 조회한 모든 count를 더해
//  accurateReservedSeats를 구하고 totalSeats에서 뺀 다음 accurateReservedSeats을 구해서 DB와 Redis에 맞춰줌.
  public void refreshLeftSeats(TicketInfo ticketInfo) {
    Integer accurateReservedSeats = reservationRepository.sumCountByTicketInfoId(ticketInfo.getId());
    if (accurateReservedSeats == null) {
      throw new CustomException(ExceptionType.NOT_FOUND_RESERVATION_EXCEPTION);
    }
    int accurateLeftSeats = ticketInfo.getTotalSeats() - accurateReservedSeats;
    ticketInfo.setLeftSeats(accurateLeftSeats);
//    기존에 캐시가 있으면 덮어 쓰고 없으면 그냥 무시
    if (hasLeftSeatsInRedis(ticketInfo.getId())){
      String key = "ls" + ticketInfo.getId();
      redisTemplate.opsForValue().set(key, accurateLeftSeats);
    }
  }
}
