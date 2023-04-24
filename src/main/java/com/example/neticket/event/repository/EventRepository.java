package com.example.neticket.event.repository;

import com.example.neticket.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

//  Event가 가진 ticketInfo에서 isAvailable이 true인 것을 가져오고 날짜가 가장 빠른순으로 정렬해서 page로 반환
  @Query("SELECT e FROM Event e JOIN e.ticketInfo t WHERE t.isAvailable = true GROUP BY e.id ORDER BY MIN(e.date) ASC")
  Page<Event> findAllByAvailableOrderByticketInfoDate(Pageable pageable);

//  keyword로 Event의 title과 place에서 검색해 page로 반환. 예매가능한게 먼저 뜨고 가장 최근에 등록된 공연 순으로 정렬된다.
  @Query("SELECT e FROM Event e JOIN e.ticketInfo t WHERE (e.title LIKE %:keyword% OR e.place LIKE %:keyword%) ORDER BY t.isAvailable DESC, e.id DESC")
  Page<Event> findAllByTitleOrPlaceContainingAndAvailableTickets(@Param("keyword") String keyword, Pageable pageable);

}
