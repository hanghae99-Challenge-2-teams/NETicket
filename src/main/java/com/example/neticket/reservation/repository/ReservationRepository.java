package com.example.neticket.reservation.repository;

import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUserOrderByIdDesc(User user);

  @Query("SELECT SUM(r.count) FROM Reservation r WHERE r.ticketInfoId = :ticketInfoId")
  Integer sumCountByTicketInfoId(@Param("ticketInfoId") Long ticketInfoId);


}
