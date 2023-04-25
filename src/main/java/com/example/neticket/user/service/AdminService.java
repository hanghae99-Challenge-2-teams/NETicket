package com.example.neticket.user.service;

import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.reservation.repository.RedisRepository;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.entity.UserRoleEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final RedisRepository redisRepository;
  private final TicketInfoRepository ticketInfoRepository;



  //  1. ADMIN. DB에서 남은 좌석수만 가져와서 Redis에 (key-value)형태로 저장. 이미 저장되어있으면 예외처리
  @Transactional
  public MessageResponseDto saveLeftSeatsInRedis(Long ticketInfoId, User user) {
    checkAdmin(user);
    TicketInfo ticketInfo = checkTicketInfoById(ticketInfoId);
    redisRepository.saveTicketInfoToRedis(ticketInfo);
    return new MessageResponseDto(HttpStatus.CREATED, "redis에 성공적으로 저장되었습니다.");
  }

  //  1-1. user role이 admin인지 체크
  private void checkAdmin(User user) {
    if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
      throw new CustomException(ExceptionType.USER_UNAUTHORIZED_EXCEPTION);
    }
  }

  //  1-2. ticketInfoId로 TicketInfo 확인
  private TicketInfo checkTicketInfoById(Long ticketInfoId) {
    return ticketInfoRepository.findById(ticketInfoId).orElseThrow(
        () -> new CustomException(ExceptionType.NOT_FOUND_TICKET_INFO_EXCEPTION)
    );
  }

  //  2. ADMIN. 해당하는 공연의 남은 좌석수 Redis에서 삭제(삭제되기전 모든 캐시 DB에 반영)
  @Transactional
  public MessageResponseDto deleteLeftSeatsFromRedis(Long ticketInfoId, User user) {
    checkAdmin(user);
    TicketInfo ticketInfo = checkTicketInfoById(ticketInfoId);
    redisRepository.deleteLeftSeatsInRedis(ticketInfo);
    return new MessageResponseDto(HttpStatus.OK, "redis에서 캐시가 성공적으로 삭제되었습니다.");
  }

  //  3. ADMIN. 현재 Redis에 등록된 모든 LeftSeats의 key를 리스트로 반환
  @Transactional(readOnly = true)
  public List<String> findAllLeftSeatsKeysInRedis(User user) {
    checkAdmin(user);
    return new ArrayList<>(redisRepository.findAllLeftSeatsKeysInRedis());
  }

  //  4. ADMIN. Redis와 DB의 LeftSeats를 정확한 값으로 Refresh
  @Transactional
  public MessageResponseDto refreshLeftSeats(Long ticketInfoId, User user) {
    checkAdmin(user);
    TicketInfo ticketInfo = checkTicketInfoById(ticketInfoId);
    redisRepository.refreshLeftSeats(ticketInfo);
    return new MessageResponseDto(HttpStatus.OK, "redis와 DB의 남은 좌석수를 올바르게 맞췄습니다.");
  }


}
