package com.example.neticket.reservation.repository;

import com.example.neticket.event.entity.ShowTime;
import com.example.neticket.reservation.entity.Reservation;
import com.example.neticket.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findAllByUser(User user);

}
