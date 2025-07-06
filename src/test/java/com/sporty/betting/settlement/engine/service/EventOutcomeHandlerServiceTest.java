package com.sporty.betting.settlement.engine.service;

import static org.mockito.Mockito.*;

import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.BetSettlementProducer;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventOutcomeHandlerServiceTest {

  @Mock private BetService betService;

  @Mock private BetSettlementProducer betSettlementProducer;

  @InjectMocks private EventOutcomeHandlerService handlerService;

  @Test
  void processEventOutcome_givenBetsAndMatchingWinnerId_thenOnlySendsWinningBetToRocketMQ() {
    // GIVEN
    var winnerId = "novak-djokovic";
    var eventId = "match-001";

    Bet winningBet =
        new Bet(
            UUID.randomUUID(),
            "user-1",
            eventId,
            "match-winner",
            winnerId,
            new BigDecimal("100.00"));
    Bet losingBet =
        new Bet(
            UUID.randomUUID(),
            "user-2",
            eventId,
            "match-winner",
            "carlos-alcaraz",
            new BigDecimal("50.00"));
    var kafkaMessage = new EventOutcomeKafkaMessage(eventId, "match-winner", winnerId);

    when(betService.findBetsByEventId(eventId)).thenReturn(List.of(winningBet, losingBet));

    // WHEN
    handlerService.processEventOutcome(kafkaMessage);

    // THEN
    verify(betSettlementProducer).sendSettlement(winningBet);
    verify(betSettlementProducer, never()).sendSettlement(losingBet);
    verify(betService).findBetsByEventId(eventId);
  }

  @Test
  void processEventOutcome_givenNoBetsForEvent_thenDoesNothing() {
    // GIVEN
    var kafkaMessage =
        new EventOutcomeKafkaMessage("non-existing-event", "event-name", "any-winner");
    when(betService.findBetsByEventId("non-existing-event")).thenReturn(List.of());

    // WHEN
    handlerService.processEventOutcome(kafkaMessage);

    // THEN
    verifyNoInteractions(betSettlementProducer);
    verify(betService).findBetsByEventId("non-existing-event");
  }
}
