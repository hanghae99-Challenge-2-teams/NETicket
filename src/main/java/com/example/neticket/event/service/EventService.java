package com.example.neticket.event.service;

import com.example.neticket.event.dto.DetailEventResponseDto;
import com.example.neticket.event.dto.EventRequestDto;
import com.example.neticket.event.dto.EventResponseDto;
import com.example.neticket.event.dto.MessageResponseDto;
import com.example.neticket.event.entity.Event;
import com.example.neticket.event.entity.TicketInfo;
import com.example.neticket.event.repository.EventRepository;
import com.example.neticket.event.repository.TicketInfoRepository;
import com.example.neticket.exception.CustomException;
import com.example.neticket.exception.ExceptionType;
import com.example.neticket.user.entity.User;
import com.example.neticket.user.entity.UserRoleEnum;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;
  private final TicketInfoRepository ticketInfoRepository;
  private final S3Client s3Client;
  private final String bucketName;
  private final RedisTemplate<String, DetailEventResponseDto> redisTemplate;

  // S3 이미지 업로드 메서드
  private String S3ImageUpload(MultipartFile image) {
    String key = UUID.randomUUID() + "_" + image.getOriginalFilename();

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key("uploaded-image/" + key)
        .contentType(image.getContentType())
        .contentLength(image.getSize())
        .build();
    try {
      s3Client.putObject(request,
          RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
    } catch (SdkClientException | IOException e) {
      throw new CustomException(ExceptionType.IO_EXCEPTION);
    }
    return key;

  }

  // S3 이미지 삭제 메서드
  private void S3ImageDelete(String img) {
    String div = "uploaded-image";
    s3Client.deleteObject(
        DeleteObjectRequest.builder().bucket(bucketName).key(div + "/" + img).build());

  }

  // 1.메인페이지 조회
  @Transactional(readOnly = true)
  public Page<EventResponseDto> getEvents(int page) {
    Pageable pageable = PageRequest.of(page, 4);
    return eventRepository.findAllByAvailableOrderByticketInfoDate(pageable)
        .map(EventResponseDto::new);

  }

  // 2.상세 페이지 조회
  @Transactional(readOnly = true)
  public DetailEventResponseDto getDetailEvent(Long eventId) {
    String cacheKey = "DetailEventResponseDto::" + eventId;
    DetailEventResponseDto detailEventResponseDto = null;

    try {
      detailEventResponseDto = redisTemplate.opsForValue().get(cacheKey);
      if (detailEventResponseDto == null) {
        detailEventResponseDto = getEventInfo(eventId);
        redisTemplate.opsForValue().set(cacheKey, detailEventResponseDto, Duration.ofHours(1));
      }
    } catch (Exception e) {
      detailEventResponseDto = getEventInfo(eventId);
    }
    return detailEventResponseDto;

  }

  // 공연상세정보 조회 메서드
  private DetailEventResponseDto getEventInfo(Long eventId) {
    return eventRepository.findById(eventId).map(DetailEventResponseDto::new)
        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND_EVENT_EXCEPTION));

  }

  // 3.공연 추가하기
  @Transactional
  public MessageResponseDto addEvent(EventRequestDto eventRequestDto, User user,
      MultipartFile image) {
    checkAdmin(user);
    String key = S3ImageUpload(image);
    Event event = eventRepository.save(new Event(eventRequestDto, key));
    ticketInfoRepository.save(new TicketInfo(eventRequestDto, event));

    return new MessageResponseDto(HttpStatus.CREATED, "공연 추가 완료했습니다.");

  }

  // 4.검색기능
  @Transactional(readOnly = true)
  public Page<EventResponseDto> searchEvents(String keyword, int page) {
    Pageable pageable = PageRequest.of(page, 4);
    return eventRepository.findAllByTitleOrPlaceContainingAndAvailableTickets(keyword, pageable)
        .map(EventResponseDto::new);

  }

  // 관리자 체크 메서드
  private void checkAdmin(User user) {
    if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
      throw new CustomException(ExceptionType.USER_UNAUTHORIZED_EXCEPTION);
    }
  }

}
