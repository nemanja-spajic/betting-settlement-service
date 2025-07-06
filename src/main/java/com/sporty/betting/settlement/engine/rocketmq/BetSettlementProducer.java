package com.sporty.betting.settlement.engine.rocketmq;

import static com.sporty.betting.settlement.common.logging.LogCode.FAILED_SERIALIZATION;
import static com.sporty.betting.settlement.common.logging.LogCode.ROCKETMQ_SETTLING_BET;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.engine.mapper.BetSettlementMapper;
import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.rocketmq.message.BetSettlementRocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BetSettlementProducer {
  private final ObjectMapper objectMapper;

  public void sendSettlement(Bet bet) {
    try {
      BetSettlementRocketMessage msg = BetSettlementMapper.fromBet(bet);
      String json = objectMapper.writeValueAsString(msg);

      log.info("{}, json={}", ROCKETMQ_SETTLING_BET, json);
    } catch (JsonProcessingException e) {
      log.error("{}, rocketMq", FAILED_SERIALIZATION, e);
    }
  }
}
