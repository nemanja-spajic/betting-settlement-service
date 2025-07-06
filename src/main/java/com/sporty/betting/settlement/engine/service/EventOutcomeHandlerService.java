package com.sporty.betting.settlement.engine.service;

import static com.sporty.betting.settlement.common.logging.LogCode.BET_LOST_SKIPPING;
import static com.sporty.betting.settlement.common.logging.LogCode.BET_WON_SENDING_TO_ROCKETMQ;

import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.BetSettlementProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeHandlerService {

  private final BetService betService;
  private final BetSettlementProducer betSettlementProducer;

  public void processEventOutcome(EventOutcomeKafkaMessage message) {
    betService
        .findBetsByEventId(message.eventId())
        .forEach(
            bet -> {
              if (bet.getEventWinnerId().equals(message.eventWinnerId())) {
                processWon(bet);
              } else {
                processLoss(bet);
              }
            });
  }

  private void processWon(Bet bet) {
    log.info("{}, betId={}", BET_WON_SENDING_TO_ROCKETMQ, bet.getId());
    betSettlementProducer.sendSettlement(bet);
  }

  private void processLoss(Bet bet) {
    log.info("{}, betId={}", BET_LOST_SKIPPING, bet.getId());
  }
}
