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

  // DB로 예매하기 시 ticketInfo를 조회할때 비관적 락 적용
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM TicketInfo t WHERE t.id = :id")
  Optional<TicketInfo> findByIdWithLock(@Param("id") Long id);

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
