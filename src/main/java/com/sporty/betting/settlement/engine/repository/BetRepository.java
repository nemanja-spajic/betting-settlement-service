package com.sporty.betting.settlement.engine.repository;

import com.sporty.betting.settlement.engine.model.Bet;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, UUID> {
  List<Bet> findByEventId(String eventId);
}
