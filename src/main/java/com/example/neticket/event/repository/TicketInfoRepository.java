package com.example.neticket.event.repository;

import com.example.neticket.event.entity.TicketInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketInfoRepository extends JpaRepository<TicketInfo, Long> {

}
