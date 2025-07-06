package com.sporty.betting.settlement.publisher.kafka;

import static com.sporty.betting.settlement.common.kafka.KafkaTopics.EVENT_OUTCOMES;
import static com.sporty.betting.settlement.common.logging.LogCode.EVENT_PRODUCED;
import static com.sporty.betting.settlement.common.logging.LogCode.FAILED_SERIALIZATION;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;
import com.sporty.betting.settlement.publisher.mapper.EventOutcomeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void sendEventOutcome(EventOutcomeDto dto) {
    try {
      String json = objectMapper.writeValueAsString(EventOutcomeMapper.fromDto(dto));
      kafkaTemplate.send(EVENT_OUTCOMES, json);

      log.info("{}, topic={}, eventId={}", EVENT_PRODUCED, EVENT_OUTCOMES, dto.eventId());
    } catch (JsonProcessingException e) {
      log.error("{}, topic={}", FAILED_SERIALIZATION, EVENT_OUTCOMES, e);
    }
  }
}
