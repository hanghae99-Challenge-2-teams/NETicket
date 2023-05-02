package com.example.neticket.event.repository;

import com.example.neticket.event.entity.TicketInfo;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketInfoRepository extends JpaRepository<TicketInfo, Long> {

  // DB에 원자적 연산으로 예매가 가능한 티켓의 남은 좌석수를 차감. 남은 좌석수가 count보다 많을때만 차감. 바뀌는 row 수를 반환.
  @Modifying
  @Query("UPDATE TicketInfo t SET t.leftSeats = t.leftSeats - :count WHERE t.id = :id AND t.isAvailable = true AND t.leftSeats >= :count")
  int decrementLeftSeats(@Param("id") Long id, @Param("count") int count);

  // id로 ticketInfo의 isAvailable 값만 조회
  @Query("SELECT t.isAvailable FROM TicketInfo t WHERE t.id = :id")
  Optional<Boolean> findIsAvailableById(@Param("id") Long id);

  // 공연일이 오늘인 ticketInfo를 가져옴
  @Query("SELECT t FROM TicketInfo t JOIN Event e ON t.event.id = e.id WHERE DATE(e.date) = CURRENT_DATE")
  List<TicketInfo> findTicketInfoByEventDate();

  // 티켓오픈일이 오늘인 ticketInfo를 가져옴
  @Query("SELECT t FROM TicketInfo t WHERE DATE(t.openDate) = CURRENT_DATE")
  List<TicketInfo> findTicketInfoByOpenDate();

  // 남은 좌석수를 총 좌석수로 변경
  @Modifying
  @Query(value = "UPDATE ticket_info SET left_seats = total_seats", nativeQuery = true)
  void updateLeftSeatsToTotalSeats();
}
