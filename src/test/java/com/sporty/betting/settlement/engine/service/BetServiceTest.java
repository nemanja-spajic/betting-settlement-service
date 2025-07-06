package com.sporty.betting.settlement.engine.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.repository.BetRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

  @Mock private BetRepository betRepository;

  @InjectMocks private BetService betService;

  @Test
  void findBetsByEventId_givenExistingEventId_thenReturnsMatchingBets() {
    // GIVEN
    String eventId = "wimbledon-2025-final";

    Bet bet1 =
        new Bet(
            UUID.randomUUID(),
            "user-1",
            eventId,
            "match-winner",
            "novak-djokovic",
            new BigDecimal("100"));
    Bet bet2 =
        new Bet(
            UUID.randomUUID(),
            "user-2",
            eventId,
            "match-winner",
            "carlos-alcaraz",
            new BigDecimal("150"));
    List<Bet> expectedBets = List.of(bet1, bet2);

    when(betRepository.findByEventId(eventId)).thenReturn(expectedBets);

    // WHEN
    List<Bet> actualBets = betService.findBetsByEventId(eventId);

    // THEN
    assertThat(actualBets).containsExactlyElementsOf(expectedBets);
    verify(betRepository).findByEventId(eventId);
  }

  @Test
  void findBetsByEventId_givenNoBets_thenReturnsEmptyList() {
    // GIVEN
    String eventId = "non-existing-event";
    when(betRepository.findByEventId(eventId)).thenReturn(List.of());

    // WHEN
    List<Bet> result = betService.findBetsByEventId(eventId);

    // THEN
    assertThat(result).isEmpty();
    verify(betRepository).findByEventId(eventId);
  }
}
