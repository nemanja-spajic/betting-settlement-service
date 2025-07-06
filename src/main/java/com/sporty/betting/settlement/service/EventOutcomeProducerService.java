package com.sporty.betting.settlement.service;

import com.sporty.betting.settlement.dto.EventOutcomeDto;
import com.sporty.betting.settlement.kafka.EventOutcomeProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeProducerService {

    private final EventOutcomeProducer producer;

    public void publishEventOutcome(EventOutcomeDto dto) {
        producer.sendEventOutcome(dto); // You could add validation or transformation here
    }
}
