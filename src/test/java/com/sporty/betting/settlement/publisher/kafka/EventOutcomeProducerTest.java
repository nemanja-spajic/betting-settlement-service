package com.sporty.betting.settlement.publisher.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;
import com.sporty.betting.settlement.publisher.fixtures.EventOutcomeDtoMother;
import com.sporty.betting.settlement.publisher.mapper.EventOutcomeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class EventOutcomeProducerTest {

  @Mock private KafkaTemplate<String, String> kafkaTemplate;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private EventOutcomeProducer producer;

  private final EventOutcomeDto validDto = EventOutcomeDtoMother.valid();

  @Test
  void sendEventOutcome_givenValidDto_thenSendsToKafka() throws Exception {
    // GIVEN
    String expectedJson = EventOutcomeDtoMother.validJson();
    given(objectMapper.writeValueAsString(any())).willReturn(expectedJson);

    // WHEN
    producer.sendEventOutcome(validDto);

    // THEN
    verify(objectMapper).writeValueAsString(EventOutcomeMapper.fromDto(validDto));
    verify(kafkaTemplate).send("event-outcomes", validDto.eventId(), expectedJson);
  }

  @Test
  void sendEventOutcome_whenSerializationFails_thenLogsErrorAndDoesNotSend() throws Exception {
    // GIVEN
    given(objectMapper.writeValueAsString(any()))
        .willThrow(new JsonProcessingException("Serialization error") {});

    // WHEN
    producer.sendEventOutcome(validDto);

    // THEN
    verify(objectMapper).writeValueAsString(EventOutcomeMapper.fromDto(validDto));
    verifyNoInteractions(kafkaTemplate);
  }
}
