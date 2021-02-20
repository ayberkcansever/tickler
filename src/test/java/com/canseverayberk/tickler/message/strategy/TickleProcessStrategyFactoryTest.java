package com.canseverayberk.tickler.message.strategy;

import com.canseverayberk.tickler.model.Tickle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class TickleProcessStrategyFactoryTest {

    @Autowired
    TickleProcessStrategyFactory tickleProcessStrategyFactory;

    @MockBean
    ApplicationContext applicationContext;

    @MockBean
    KafkaTemplate kafkaTemplate;

    @MockBean
    TickleRestCallbackProcessStrategy tickleRestCallbackProcessStrategy;

    @MockBean
    TickleKafkaCallbackProcessStrategy tickleKafkaCallbackProcessStrategy;

    @MockBean
    TickleLogStrategy tickleLogStrategy;

    @ParameterizedTest
    @CsvSource({
            "payload,restCallback,,1,,com.canseverayberk.tickler.message.strategy.TickleRestCallbackProcessStrategy",
            "payload,,kafkaCallbackTopic,1,,com.canseverayberk.tickler.message.strategy.TickleKafkaCallbackProcessStrategy",
            "payload,,,1,,com.canseverayberk.tickler.message.strategy.TickleLogStrategy"
    })
    void should_get_processor(String payload, String restCallback, String kafkaCallbackTopic,
                                  Long ttl, LocalDateTime expirationDate, String expectedStrategyClass) throws ClassNotFoundException {
        // given
        Tickle tickle = Tickle.builder()
                .payload(payload)
                .ttl(ttl)
                .kafkaCallbackTopic(kafkaCallbackTopic)
                .restCallbackUrl(restCallback)
                .expirationDate(expirationDate)
                .build();

        // when
        when(applicationContext.getBean(TickleRestCallbackProcessStrategy.class)).thenReturn(tickleRestCallbackProcessStrategy);
        when(applicationContext.getBean(TickleKafkaCallbackProcessStrategy.class)).thenReturn(tickleKafkaCallbackProcessStrategy);
        when(applicationContext.getBean(TickleLogStrategy.class)).thenReturn(tickleLogStrategy);
        TickleProcessStrategy processor = tickleProcessStrategyFactory.getProcessor(tickle);

        // then
        assertThat(processor).isInstanceOf(Class.forName(expectedStrategyClass));
    }

}