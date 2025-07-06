package com.sporty.betting.settlement.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EventOutcomeDto(
    @NotBlank(message = "eventId must not be blank") String eventId,
    @NotBlank(message = "eventName must not be blank") String eventName,
    @NotBlank(message = "eventWinnerId must not be blank") String eventWinnerId) {}
