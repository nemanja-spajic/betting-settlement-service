package com.sporty.betting.settlement.engine.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.message.BetSettlementRocketMessage;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BetSettlementMapperTest {

  @Test
  void fromBet_givenValidBet_thenMapsCorrectly() {
    // GIVEN
    UUID betId = UUID.randomUUID();
    Bet bet =
        new Bet(
            betId, "user-1", "match-001", "market-123", "novak-djokovic", new BigDecimal("150.00"));

    // WHEN
    BetSettlementRocketMessage message = BetSettlementMapper.fromBet(bet);

    // THEN
    assertThat(message.betId()).isEqualTo(bet.getId());
    assertThat(message.userId()).isEqualTo(bet.getUserId());
    assertThat(message.eventId()).isEqualTo(bet.getEventId());
    assertThat(message.betAmount()).isEqualByComparingTo(bet.getBetAmount());
  }
}
