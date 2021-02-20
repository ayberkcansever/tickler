package com.canseverayberk.tickler.model;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TickleTest {

    @ParameterizedTest
    @CsvSource({
            "payload,restCallback,,10,,true",
            "payload,restCallback,,,2000-01-01T00:00:00,true",
            "payload,,kafkaCallbackTopic,,2000-01-01T00:00:00,true",
            ",,kafkaCallbackTopic,,2000-01-01T00:00:00,false",
            "payload,,,10,,false",
            "payload,restCallback,,,,false"
    })
    void should_test_tickle_expiration_valid(String payload, String restCallback, String kafkaCallbackTopic,
                                             Long ttl, LocalDateTime expirationDate, String result) {
        // given
        Tickle tickle = Tickle.builder()
                .payload(payload)
                .ttl(ttl)
                .kafkaCallbackTopic(kafkaCallbackTopic)
                .restCallbackUrl(restCallback)
                .expirationDate(expirationDate)
                .build();

        // when
        boolean valid = tickle.valid();

        // then
        assertThat(valid).isEqualTo(Boolean.valueOf(result));
    }

}