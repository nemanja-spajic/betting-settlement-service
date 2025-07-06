package com.sporty.betting.settlement.kafka.message;

public record EventOutcomeKafkaMessage(
        String eventId,
        String eventName,
        String eventWinnerId
) {}

