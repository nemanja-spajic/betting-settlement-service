package com.sporty.betting.settlement.engine.kafka;

import static com.sporty.betting.settlement.common.kafka.KafkaTopics.EVENT_OUTCOMES;
import static com.sporty.betting.settlement.common.logging.LogCode.EVENT_PROCESSED;
import static com.sporty.betting.settlement.common.logging.LogCode.FAILED_DESERIALIZATION;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.engine.service.EventOutcomeHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeConsumer {

  private final EventOutcomeHandlerService handlerService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = EVENT_OUTCOMES, groupId = "settlement-group")
  public void consume(String message) {
    try {
      EventOutcomeKafkaMessage msg =
          objectMapper.readValue(message, EventOutcomeKafkaMessage.class);
      handlerService.processEventOutcome(msg);

      log.info("{}, topic={}, eventId={}", EVENT_PROCESSED, EVENT_OUTCOMES, msg.eventId());
    } catch (JsonProcessingException e) {
      log.error("{}, topic={}", FAILED_DESERIALIZATION, EVENT_OUTCOMES, e);
    }
  }
}
