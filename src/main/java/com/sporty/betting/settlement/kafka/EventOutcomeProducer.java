package com.sporty.betting.settlement.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.dto.EventOutcomeDto;
import com.sporty.betting.settlement.mapper.EventOutcomeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.sporty.betting.settlement.kafka.KafkaTopics.EVENT_OUTCOMES;
import static com.sporty.betting.settlement.logging.LogCode.EVENT_PRODUCED;
import static com.sporty.betting.settlement.logging.LogCode.FAILED_DESERIALIZATION;
import static com.sporty.betting.settlement.logging.LogCode.FAILED_SERIALIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendEventOutcome(EventOutcomeDto dto) {
        try {
            String json = objectMapper.writeValueAsString(
                    EventOutcomeMapper.fromDto(dto)
            );
            kafkaTemplate.send(EVENT_OUTCOMES, json);

            log.info("{}, topic={}, eventId={}", EVENT_PRODUCED, EVENT_OUTCOMES, dto.eventId());
        } catch (JsonProcessingException e) {
            log.error("{}, topic={}", FAILED_SERIALIZATION, EVENT_OUTCOMES, e);
        }
    }
}
