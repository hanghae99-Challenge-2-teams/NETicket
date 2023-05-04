package com.example.neticket.event.repository;

import com.example.neticket.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom,
    QuerydslPredicateExecutor<Event> {

  // ticketInfo에서 isAvailable이 true인 Event를 가져오고 날짜가 가장 빠른순으로 정렬해서 page로 반환
  @Query("SELECT e FROM Event e JOIN e.ticketInfo t WHERE t.isAvailable = true GROUP BY e.id ORDER BY e.date ASC")
  Page<Event> findAllByAvailableOrderByticketInfoDate(Pageable pageable);

//  // Event의 title과 place에서 keyword로 검색해 page로 반환. 예매가능한 공연을 먼저 정렬 후 가장 날짜가 가장 빠른순으로 정렬
//  @Query("SELECT e FROM Event e JOIN e.ticketInfo t WHERE (e.title LIKE %:keyword% OR e.place LIKE %:keyword%) ORDER BY t.isAvailable DESC, e.date ASC")
//  Page<Event> findAllByTitleOrPlaceContainingAndAvailableTickets(@Param("keyword") String keyword, Pageable pageable);

}
