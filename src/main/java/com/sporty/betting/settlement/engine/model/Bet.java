package com.sporty.betting.settlement.engine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "bets",
    indexes = {@Index(name = "idx_bets_event_id", columnList = "eventId")})
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Bet {

  @Id private UUID id;

  private String userId;
  private String eventId;
  private String eventMarketId;
  private String eventWinnerId;
  private BigDecimal betAmount;
}
