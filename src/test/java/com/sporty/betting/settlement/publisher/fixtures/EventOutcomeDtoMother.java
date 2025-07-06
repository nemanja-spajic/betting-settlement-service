package com.sporty.betting.settlement.publisher.fixtures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;

public class EventOutcomeDtoMother {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private EventOutcomeDtoMother() {
    // prevent instantiation
  }

  public static EventOutcomeDto valid() {
    return EventOutcomeDto.builder()
        .eventId("match-001")
        .eventName("match-winner")
        .eventWinnerId("novak-djokovic")
        .build();
  }

  public static EventOutcomeDto withNullEventId() {
    return EventOutcomeDto.builder()
        .eventId(null)
        .eventName("match-winner")
        .eventWinnerId("novak-djokovic")
        .build();
  }

  public static EventOutcomeDto withNullEventName() {
    return EventOutcomeDto.builder()
        .eventId("match-001")
        .eventName(null)
        .eventWinnerId("novak-djokovic")
        .build();
  }

  public static EventOutcomeDto withNullWinnerId() {
    return EventOutcomeDto.builder()
        .eventId("match-001")
        .eventName("match-winner")
        .eventWinnerId(null)
        .build();
  }

  public static EventOutcomeDto withAllFieldsNull() {
    return EventOutcomeDto.builder().eventId(null).eventName(null).eventWinnerId(null).build();
  }

  public static String validJson() {
    return toJson(valid());
  }

  public static String withNullEventIdJson() {
    return toJson(withNullEventId());
  }

  private static String toJson(EventOutcomeDto dto) {
    try {
      return objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize DTO to JSON", e);
    }
  }
}
