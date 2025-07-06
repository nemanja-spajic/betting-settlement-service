package com.sporty.betting.settlement.service;

import com.sporty.betting.settlement.dto.EventOutcomeDto;
import com.sporty.betting.settlement.model.Bet;
import com.sporty.betting.settlement.rocketmq.BetSettlementProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sporty.betting.settlement.logging.LogCode.BET_LOST_SKIPPING;
import static com.sporty.betting.settlement.logging.LogCode.BET_WON_SENDING_TO_ROCKETMQ;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeHandlerService {


    private final BetService betService;
    private final BetSettlementProducer betSettlementProducer;

    public void processEventOutcome(EventOutcomeDto dto) {
        List<Bet> bets = betService.findBetsByEventId(dto.eventId());
        for (Bet bet : bets) {
            if (bet.getEventWinnerId().equals(dto.eventWinnerId())) {
                log.info("{}, betId={}", BET_WON_SENDING_TO_ROCKETMQ, bet.getId());
                betSettlementProducer.sendSettlement(bet);
            } else {
                log.info("{}, betId={}", BET_LOST_SKIPPING, bet.getId());
            }
        }
    }
}
