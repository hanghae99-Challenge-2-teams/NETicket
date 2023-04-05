package com.example.neticket.event.repository;

import com.example.neticket.event.entity.TicketInfo;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface TicketInfoRepository extends JpaRepository<TicketInfo, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM TicketInfo t WHERE t.id = :id")
  Optional<TicketInfo> findByIdWithLock(@Param("id") Long id);
}
