package com.sporty.betting.settlement.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.dto.EventOutcomeDto;
import com.sporty.betting.settlement.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.mapper.EventOutcomeMapper;
import com.sporty.betting.settlement.service.EventOutcomeHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.sporty.betting.settlement.kafka.KafkaTopics.EVENT_OUTCOMES;
import static com.sporty.betting.settlement.logging.LogCode.EVENT_PROCESSED;
import static com.sporty.betting.settlement.logging.LogCode.FAILED_DESERIALIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeConsumer {

    private final EventOutcomeHandlerService handlerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = EVENT_OUTCOMES, groupId = "settlement-group")
    public void consume(String message) {
        try {
            EventOutcomeKafkaMessage msg = objectMapper.readValue(message, EventOutcomeKafkaMessage.class);
            EventOutcomeDto dto = EventOutcomeMapper.toDto(msg);
            handlerService.processEventOutcome(dto);

            log.info("{}, topic={}, eventId={}", EVENT_PROCESSED, EVENT_OUTCOMES, dto.eventId());
        } catch (JsonProcessingException e) {
            log.error("{}, topic={}", FAILED_DESERIALIZATION, EVENT_OUTCOMES, e);
        }
    }
}
