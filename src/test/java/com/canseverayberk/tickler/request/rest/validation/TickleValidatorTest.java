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
            ",restCallback,,1,,false,{invalidTickle.payload.message}",
            "payload,restCallback,,,,false,{invalidTickle.expiration.message}",
            "payload,restCallback,,1,2000-01-01T00:00:00,false,{invalidTickle.expiration.message}",
            "payload,restCallback,,-2,,false,{invalidTickle.ttl.message}",
            "payload,restCallback,,,2000-01-01T00:00:00,false,{invalidTickle.expirationDate.message}",
            "payload,restCallback,,1,,true,"
    })
    void should_invalidate_tickle(String payload, String restCallback, String kafkaCallbackTopic,
                                  Long ttl, LocalDateTime expirationDate, String isValid, String errMsg) {
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
        Boolean expectedValid = Boolean.valueOf(isValid);
        assertThat(valid).isEqualTo(expectedValid);

        verify(constraintValidatorContext, times(1)).disableDefaultConstraintViolation();

        if(!expectedValid) {
            verify(constraintValidatorContext).buildConstraintViolationWithTemplate(messageCaptor.capture());
            verify(constraintViolationBuilder, times(1)).addConstraintViolation();
            assertThat(messageCaptor.getValue()).isEqualTo(errMsg);
        } else {
            verifyNoInteractions(constraintValidatorContext.buildConstraintViolationWithTemplate(any()));
        }
    }

}