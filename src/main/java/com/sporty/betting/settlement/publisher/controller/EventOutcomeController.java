package com.sporty.betting.settlement.publisher.controller;

import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;
import com.sporty.betting.settlement.publisher.service.EventOutcomeProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventOutcomeController {

  private final EventOutcomeProducerService service;

  @PostMapping("/outcome")
  public ResponseEntity<Void> publishOutcome(@RequestBody EventOutcomeDto dto) {
    service.publishEventOutcome(dto);
    return ResponseEntity.ok().build();
  }
}
