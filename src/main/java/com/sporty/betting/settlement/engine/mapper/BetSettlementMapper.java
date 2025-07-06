package com.sporty.betting.settlement.engine.mapper;

import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.message.BetSettlementRocketMessage;

public class BetSettlementMapper {

  private BetSettlementMapper() {}

  public static BetSettlementRocketMessage fromBet(Bet bet) {
    return new BetSettlementRocketMessage(
        bet.getId(), bet.getUserId(), bet.getEventId(), bet.getBetAmount());
  }
}
