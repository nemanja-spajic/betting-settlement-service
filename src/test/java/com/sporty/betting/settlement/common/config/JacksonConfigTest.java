package com.sporty.betting.settlement.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfig.class)
class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    static class Sample {
        public String name;
        public String description;

        public Sample() {}
        public Sample(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    @Test
    void objectMapper_shouldIgnoreUnknownProperties_whenDeserializing() throws Exception {
        // GIVEN
        String json = """
        {
          "name": "Test Name",
          "unknownField": "shouldBeIgnored"
        }
        """;

        // WHEN
        Sample result = objectMapper.readValue(json, Sample.class);

        // THEN
        assertThat(result.name).isEqualTo("Test Name");
        assertThat(result.description).isNull();
    }

    @Test
    void objectMapper_shouldExcludeNullFields_whenSerializing() throws Exception {
        // GIVEN
        Sample sample = new Sample("Test Name", null);

        // WHEN
        String json = objectMapper.writeValueAsString(sample);

        // THEN
        assertThat(json).contains("\"name\"");
        assertThat(json).doesNotContain("description");
    }
}

