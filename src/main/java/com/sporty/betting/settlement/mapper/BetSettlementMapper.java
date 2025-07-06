package com.sporty.betting.settlement.mapper;

import com.sporty.betting.settlement.model.Bet;
import com.sporty.betting.settlement.rocketmq.message.BetSettlementRocketMessage;

public class BetSettlementMapper {
    public static BetSettlementRocketMessage fromBet(Bet bet) {
        return new BetSettlementRocketMessage(
                bet.getId(),
                bet.getUserId(),
                bet.getEventId(),
                bet.getBetAmount()
        );
    }
}

