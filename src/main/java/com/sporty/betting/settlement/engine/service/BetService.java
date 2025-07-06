package com.sporty.betting.settlement.engine.service;

import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.repository.BetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BetService {

  private final BetRepository repository;

  public List<Bet> findBetsByEventId(String eventId) {
    return repository.findByEventId(eventId);
  }
}
