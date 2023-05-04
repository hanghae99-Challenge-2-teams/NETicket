package com.example.neticket.event.repository;

import com.example.neticket.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {

  Page<Event> findAllByTitleOrPlaceContainingAndAvailableTickets(String keyword, Pageable pageable);

}
