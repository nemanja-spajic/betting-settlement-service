package com.sporty.betting.settlement.common.kafka.message;

public record EventOutcomeKafkaMessage(String eventId, String eventName, String eventWinnerId) {}
