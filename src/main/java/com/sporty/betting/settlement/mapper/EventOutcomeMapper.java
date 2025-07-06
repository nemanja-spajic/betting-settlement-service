package com.sporty.betting.settlement.mapper;

import com.sporty.betting.settlement.dto.EventOutcomeDto;
import com.sporty.betting.settlement.kafka.message.EventOutcomeKafkaMessage;

public class EventOutcomeMapper {
    public static EventOutcomeKafkaMessage fromDto(EventOutcomeDto dto) {
        return new EventOutcomeKafkaMessage(dto.eventId(), dto.eventName(), dto.eventWinnerId());
    }

    public static EventOutcomeDto toDto(EventOutcomeKafkaMessage msg) {
        return new EventOutcomeDto(msg.eventId(), msg.eventName(), msg.eventWinnerId());
    }
}

