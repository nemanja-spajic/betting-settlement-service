package com.sporty.betting.settlement.service;

import com.sporty.betting.settlement.model.Bet;
import com.sporty.betting.settlement.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository repository;

    public List<Bet> findBetsByEventId(String eventId) {
        return repository.findByEventId(eventId);
    }

    public void saveBet(Bet bet) {
        repository.save(bet);
    }
}
