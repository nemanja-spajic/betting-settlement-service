package com.sporty.betting.settlement.engine.service;

import static com.sporty.betting.settlement.common.logging.LogCode.BET_LOST_SKIPPING;
import static com.sporty.betting.settlement.common.logging.LogCode.BET_WON_SENDING_TO_ROCKETMQ;

import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.BetSettlementProducer;
import java.util.List;
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
    List<Bet> bets = betService.findBetsByEventId(message.eventId());
    for (Bet bet : bets) {
      if (bet.getEventWinnerId().equals(message.eventWinnerId())) {
        log.info("{}, betId={}", BET_WON_SENDING_TO_ROCKETMQ, bet.getId());
        betSettlementProducer.sendSettlement(bet);
      } else {
        log.info("{}, betId={}", BET_LOST_SKIPPING, bet.getId());
      }
    }
  }
}
