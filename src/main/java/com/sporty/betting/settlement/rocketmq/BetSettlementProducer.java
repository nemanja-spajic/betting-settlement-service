package com.sporty.betting.settlement.rocketmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.mapper.BetSettlementMapper;
import com.sporty.betting.settlement.model.Bet;
import com.sporty.betting.settlement.rocketmq.message.BetSettlementRocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.sporty.betting.settlement.logging.LogCode.FAILED_SERIALIZATION;
import static com.sporty.betting.settlement.logging.LogCode.ROCKETMQ_SETTLING_BET;

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
