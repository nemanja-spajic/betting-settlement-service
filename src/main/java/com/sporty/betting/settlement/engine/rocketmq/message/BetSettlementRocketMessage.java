package com.sporty.betting.settlement.engine.rocketmq.message;

import java.math.BigDecimal;
import java.util.UUID;

public record BetSettlementRocketMessage(
    UUID betId, String userId, String eventId, BigDecimal betAmount) {}
