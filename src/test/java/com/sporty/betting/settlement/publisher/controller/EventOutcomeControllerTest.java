package com.sporty.betting.settlement.publisher.controller;

import com.sporty.betting.settlement.common.exception.GlobalExceptionHandler;
import com.sporty.betting.settlement.publisher.dto.EventOutcomeDto;
import com.sporty.betting.settlement.publisher.fixtures.EventOutcomeDtoMother;
import com.sporty.betting.settlement.publisher.service.EventOutcomeProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventOutcomeControllerTest {

    @InjectMocks
    private EventOutcomeController controller;

    @Mock
    private EventOutcomeProducerService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void publishOutcome_givenValidDto_whenPostRequest_thenReturnsOkAndCallsService() throws Exception {
        // GIVEN
        EventOutcomeDto dto = EventOutcomeDtoMother.valid();
        String json = EventOutcomeDtoMother.validJson();

        // WHEN
        mockMvc.perform(post("/api/events/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // THEN
        verify(service).publishEventOutcome(dto);
    }

    @Test
    void publishOutcome_givenMissingFields_whenPostRequest_thenReturnsBadRequest() throws Exception {
        // GIVEN
        String invalidJson = EventOutcomeDtoMother.withNullEventIdJson();

        // WHEN / THEN
        mockMvc.perform(post("/api/events/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details").isArray());
    }
}

