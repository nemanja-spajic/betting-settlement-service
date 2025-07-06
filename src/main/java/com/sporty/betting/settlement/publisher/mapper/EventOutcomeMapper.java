package com.sporty.betting.settlement.publisher.mapper;

import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;

public class EventOutcomeMapper {

  private EventOutcomeMapper() {}

  public static EventOutcomeKafkaMessage fromDto(EventOutcomeDto dto) {
    return new EventOutcomeKafkaMessage(dto.eventId(), dto.eventName(), dto.eventWinnerId());
  }
}
