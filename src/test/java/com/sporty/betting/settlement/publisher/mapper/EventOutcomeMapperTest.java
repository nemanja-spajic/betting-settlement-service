package com.sporty.betting.settlement.publisher.mapper;

import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;
import com.sporty.betting.settlement.publisher.fixtures.EventOutcomeDtoMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventOutcomeMapperTest {

    @Test
    void fromDto_givenValidDto_thenReturnsExpectedKafkaMessage() {
        // GIVEN
        EventOutcomeDto dto = EventOutcomeDtoMother.valid();

        // WHEN
        EventOutcomeKafkaMessage result = EventOutcomeMapper.fromDto(dto);

        // THEN
        assertThat(result)
                .extracting(EventOutcomeKafkaMessage::eventId, EventOutcomeKafkaMessage::eventName, EventOutcomeKafkaMessage::eventWinnerId)
                .containsExactly("match-001", "match-winner", "novak-djokovic");
    }
}

