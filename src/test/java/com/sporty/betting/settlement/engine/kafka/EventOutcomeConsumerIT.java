package com.sporty.betting.settlement.engine.kafka;

import static com.sporty.betting.settlement.common.kafka.KafkaTopics.EVENT_OUTCOMES;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.betting.settlement.common.kafka.message.EventOutcomeKafkaMessage;
import com.sporty.betting.settlement.engine.model.Bet;
import com.sporty.betting.settlement.engine.repository.BetRepository;
import com.sporty.betting.settlement.engine.rocketmq.BetSettlementProducer;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    topics = {EVENT_OUTCOMES})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
class EventOutcomeConsumerIT {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private BetRepository betRepository;

  @MockitoSpyBean private BetSettlementProducer betSettlementProducer;

  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  private KafkaTemplate<String, String> kafkaTemplate;

  private static final String EVENT_ID = "match-001";
  private static final String WINNER_ID = "novak-djokovic";

  @BeforeEach
  void setup() {
    Map<String, Object> senderProps =
        KafkaTestUtils.producerProps(embeddedKafkaBroker); // <-- fix here
    senderProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    senderProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(senderProps));

    betRepository.deleteAll();

    betRepository.save(
        new Bet(
            UUID.randomUUID(),
            "user-1",
            EVENT_ID,
            "match-winner",
            WINNER_ID,
            new BigDecimal("100.00")));
    betRepository.save(
        new Bet(
            UUID.randomUUID(),
            "user-2",
            EVENT_ID,
            "match-winner",
            "carlos-alcaraz",
            new BigDecimal("50.00")));
  }

  @Test
  void consume_givenKafkaEventOutcome_thenProcessesOnlyWinningBets() throws Exception {
    // GIVEN
    var message = new EventOutcomeKafkaMessage(EVENT_ID, "match-winner", WINNER_ID);
    var json = objectMapper.writeValueAsString(message);

    // WHEN
    kafkaTemplate.send(EVENT_OUTCOMES, json);

    // THEN
    await()
        .atMost(Duration.ofSeconds(5))
        .untilAsserted(
            () ->
                verify(betSettlementProducer, times(1))
                    .sendSettlement(
                        argThat(
                            bet ->
                                bet.getUserId().equals("user-1")
                                    && bet.getEventId().equals(EVENT_ID)
                                    && bet.getEventWinnerId().equals(WINNER_ID))));

    verify(betSettlementProducer, times(1)).sendSettlement(any());
  }
}
