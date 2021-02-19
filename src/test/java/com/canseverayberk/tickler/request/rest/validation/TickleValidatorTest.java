package com.canseverayberk.tickler.request.rest.validation;

import com.canseverayberk.tickler.model.Tickle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickleValidatorTest {

    @InjectMocks
    private TickleValidator tickleValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Captor
    ArgumentCaptor<String> messageCaptor;

    @ParameterizedTest
    @CsvSource({
            ",restCallback,,1,,{invalidTickle.payload.message}",
            "payload,,,1,,{invalidTickle.callback.message}",
            "payload,restCallback,,,,{invalidTickle.expiration.message}",
            "payload,restCallback,,-2,,{invalidTickle.ttl.message}",
            "payload,restCallback,,,2000-01-01T00:00:00,{invalidTickle.expirationDate.message}"
    })
    void should_invalidate_tickle(String payload, String restCallback, String kafkaCallbackTopic,
                                  Long ttl, LocalDateTime expirationDate, String errMsg) {
        // given
        Tickle tickle = Tickle.builder()
                .payload(payload)
                .ttl(ttl)
                .kafkaCallbackTopic(kafkaCallbackTopic)
                .restCallbackUrl(restCallback)
                .expirationDate(expirationDate)
                .build();

        // when
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilder);
        boolean valid = tickleValidator.isValid(tickle, constraintValidatorContext);

        // then
        assertThat(valid).isFalse();
        verify(constraintValidatorContext, times(1)).disableDefaultConstraintViolation();
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(messageCaptor.capture());
        verify(constraintViolationBuilder, times(1)).addConstraintViolation();
        assertThat(messageCaptor.getValue()).isEqualTo(errMsg);
    }

}