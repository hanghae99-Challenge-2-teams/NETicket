package com.example.neticket.event.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.neticket.event.dto.EventRequestDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.EventRepository;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.entity.UserRoleEnum;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private TicketInfoRepository ticketInfoRepository;

  @Mock
  private S3Client s3Client;

  @InjectMocks
  private EventService eventService;

  @Test
  @DisplayName("공연추가 테스트")
  public void testAddEvent() throws Exception {
    // given
    User user = mock(User.class);
    when(user.getRole()).thenReturn(UserRoleEnum.ADMIN);
    MockMultipartFile image = new MockMultipartFile("test.jpg", new byte[0]);

    EventRequestDto eventRequestDto = mock(EventRequestDto.class);
    when(eventRequestDto.getTitle()).thenReturn("Test Title");
    when(eventRequestDto.getPlace()).thenReturn("Test Place");
    when(eventRequestDto.getPrice()).thenReturn(10000);
    when(eventRequestDto.getDate()).thenReturn(LocalDateTime.now());
    when(eventRequestDto.getOpenDate()).thenReturn(LocalDateTime.now());
    when(eventRequestDto.getTotalSeat()).thenReturn(500);

    Event event = mock(Event.class);
    when(eventRepository.save(any(Event.class))).thenReturn(event);
    TicketInfo ticketInfo = mock(TicketInfo.class);
    when(ticketInfoRepository.save(any(TicketInfo.class))).thenReturn(ticketInfo);
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);

    // when
    MessageResponseDto response = eventService.addEvent(eventRequestDto, user, image);

    // then
    verify(eventRepository, times(1)).save(any(Event.class));
    verify(ticketInfoRepository, times(1)).save(any(TicketInfo.class));
    assertEquals("공연 추가 완료했습니다.", response.getMsg());
    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
  }

}