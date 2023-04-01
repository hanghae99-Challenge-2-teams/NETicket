package com.example.neticket.event.repository;

import com.example.neticket.event.entity.Event;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

//  Event가 가진 ShowTime에서 isAvailable이 true인 것을 가져오고 날짜가 가장 빠른순으로 정렬해서 page로 반환
  @Query("SELECT e FROM Event e JOIN e.showTimeList st WHERE st.isAvailable = true GROUP BY e.id ORDER BY MIN(st.date) ASC")
  Page<Event> findAllByAvailableOrderByShowTimeDate(Pageable pageable);

}
