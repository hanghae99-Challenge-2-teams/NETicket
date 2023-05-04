package com.example.neticket.event.repository;

import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.QEvent;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryImpl implements EventRepositoryCustom {

  @Autowired
  private JPAQueryFactory queryFactory;

  @Override
  public Page<Event> findAllByTitleOrPlaceContainingAndAvailableTickets(String keyword, Pageable pageable) {
    QEvent qEvent = QEvent.event;

    // 키워드 공백 기준으로 분할해 여러 단어로 구성된 키워드 처리
    String[] keywords = keyword.split("\\s+");

    // 검색 조건을 조합하게 하는 builder
    BooleanBuilder builder = new BooleanBuilder();

    // 각 단어에 대해, title or place에서 대소문자 구분 없이 단어를 포함하는 event를 찾는 검색 조건
    for (String word : keywords) {
      builder.and(
          qEvent.title.containsIgnoreCase(word)
              .or(qEvent.place.containsIgnoreCase(word))
      );
    }

    // 정렬 순서 설정 - 1. 예매 가능 여부, 2. 날짜별 정렬
    OrderSpecifier<Boolean> isAvailableOrder = qEvent.ticketInfo.isAvailable.desc();
    OrderSpecifier<java.time.LocalDateTime> dateOrder = qEvent.date.asc();

    // 검색 조건에 일치하는 전체 이벤트 수 계산
    long totalCount = queryFactory.selectFrom(qEvent)
        .where(builder)
        .fetchCount();

    // 검색 조건에 일치하는 이벤트를 페이지 단위로 가져옴.
    // ticketInfo를 함께 가져오기 위해 left join 사용 후 정렬 순서 적용
    List<Event> events = queryFactory.selectFrom(qEvent)
        .leftJoin(qEvent.ticketInfo)
        .fetchJoin()
        .where(builder)
        .orderBy(isAvailableOrder, dateOrder)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 결과 PageImpl 객체로 변환해 반환
    return new PageImpl<>(events, pageable, totalCount);
  }

}
