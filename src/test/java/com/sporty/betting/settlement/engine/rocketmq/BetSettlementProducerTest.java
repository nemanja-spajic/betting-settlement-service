package com.sporty.betting.settlement.engine.rocketmq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.message.BetSettlementRocketMessage;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetSettlementProducerTest {

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private BetSettlementProducer producer;

  private static final Bet VALID_BET =
      new Bet(
          UUID.randomUUID(),
          "user-1",
          "match-001",
          "market-001",
          "novak-djokovic",
          new BigDecimal("100.00"));

  @Test
  void sendSettlement_givenValidBet_thenLogsJson() throws Exception {
    // GIVEN
    String expectedJson =
        "{\"betId\":\"%s\",\"userId\":\"user-1\",\"eventId\":\"match-001\",\"betAmount\":100.00}"
            .formatted(VALID_BET.getId());

    when(objectMapper.writeValueAsString(any(BetSettlementRocketMessage.class)))
        .thenReturn(expectedJson);

    // WHEN
    producer.sendSettlement(VALID_BET);

    // THEN
    verify(objectMapper).writeValueAsString(any(BetSettlementRocketMessage.class));
  }

  @Test
  void sendSettlement_whenSerializationFails_thenLogsError() throws Exception {
    // GIVEN
    when(objectMapper.writeValueAsString(any()))
        .thenThrow(new JsonProcessingException("serialization error") {});

    // WHEN
    producer.sendSettlement(VALID_BET);

    // THEN
    verify(objectMapper).writeValueAsString(any(BetSettlementRocketMessage.class));
    // No exception should propagate; handled internally
  }
}
