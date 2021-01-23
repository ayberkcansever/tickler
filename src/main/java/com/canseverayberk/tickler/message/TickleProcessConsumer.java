package com.canseverayberk.tickler.message;

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
public class TickleProcessConsumer {

    private final TickleProcessStrategyFactory tickleProcessStrategyFactory;

    @StreamListener(KafkaEventStreams.tickleProcessInput)
    public void onTickleMessage(@Payload Tickle tickle) {
        tickleProcessStrategyFactory.getProcessor(tickle).process();
    }

}
