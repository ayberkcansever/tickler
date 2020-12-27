package com.canseverayberk.tickler.message;

import com.canseverayberk.tickler.config.KafkaEventStreams;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickleConsumer {

    private final TickleProcessor tickleProcessor;

    @StreamListener(KafkaEventStreams.tickleInput)
    public void onTickleMessage(@Payload Tickle tickle) {
        tickleProcessor.process(tickle);
    }

}
