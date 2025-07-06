package com.sporty.betting.settlement.repository;

import com.sporty.betting.settlement.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BetRepository extends JpaRepository<Bet, UUID> {
    List<Bet> findByEventId(String eventId);
}

