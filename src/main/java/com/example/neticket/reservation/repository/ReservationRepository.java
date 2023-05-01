package com.example.neticket.reservation.repository;

import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUserOrderByIdDesc(User user);

  void deleteAll();

  // 실제 예매된 좌석 수 반환
  @Query("SELECT SUM(r.count) FROM Reservation r WHERE r.ticketInfoId = :ticketInfoId")
  Integer sumCountByTicketInfoId(@Param("ticketInfoId") Long ticketInfoId);

}
