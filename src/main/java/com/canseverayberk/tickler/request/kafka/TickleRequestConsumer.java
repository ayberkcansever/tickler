package com.canseverayberk.tickler.request.kafka;

import com.canseverayberk.tickler.configuration.KafkaEventStreams;
import com.canseverayberk.tickler.message.strategy.TickleProcessStrategyFactory;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickleRequestConsumer {

    private final TickleProcessStrategyFactory tickleProcessStrategyFactory;

    @StreamListener(KafkaEventStreams.tickleRequestInput)
    public void onTickleMessage(@Payload Tickle tickle) {
        if (tickle.valid()) {
            tickleProcessStrategyFactory.getProcessor(tickle).process();
        } else {
            log.warn("Invalid tickle request consumed: {}", tickle);
        }
    }

}
