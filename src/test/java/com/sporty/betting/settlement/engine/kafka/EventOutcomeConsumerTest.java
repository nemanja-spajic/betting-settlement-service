package com.sporty.betting.settlement.engine.kafka;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.engine.service.EventOutcomeHandlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventOutcomeConsumerTest {

  @Mock private EventOutcomeHandlerService handlerService;

  private ObjectMapper objectMapper;

  @InjectMocks private EventOutcomeConsumer consumer;

  private EventOutcomeKafkaMessage validMessage;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    consumer = new EventOutcomeConsumer(handlerService, objectMapper);

    validMessage = new EventOutcomeKafkaMessage("match-001", "match-winner", "novak-djokovic");
  }

  @Test
  void consume_givenValidJsonMessage_thenCallsHandlerService() throws Exception {
    // GIVEN
    String json = objectMapper.writeValueAsString(validMessage);

    // WHEN
    consumer.consume(json);

    // THEN
    verify(handlerService).processEventOutcome(validMessage);
  }

  @Test
  void consume_givenInvalidJsonMessage_thenLogsErrorAndDoesNotCallHandler() {
    // GIVEN
    String invalidJson = "{ invalid json }";

    // WHEN
    consumer.consume(invalidJson);

    // THEN
    verifyNoInteractions(handlerService);
  }
}
