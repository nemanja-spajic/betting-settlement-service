package com.sporty.betting.settlement.publisher.service;

import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;
import com.sporty.betting.settlement.publisher.kafka.EventOutcomeProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOutcomeProducerService {

  private final EventOutcomeProducer producer;

  public void publishEventOutcome(EventOutcomeDto dto) {
    producer.sendEventOutcome(dto);
  }
}
