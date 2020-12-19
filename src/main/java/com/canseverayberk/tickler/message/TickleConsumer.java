package com.canseverayberk.tickler.message;

import com.canseverayberk.tickler.config.KafkaEventStreams;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.canseverayberk.tickler.TicklerApplication.I_AM_LEADER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickleConsumer {

    private final TickleProcessor tickleProcessor;

    @StreamListener(KafkaEventStreams.tickleInput)
    public void onTickleMessage(@Payload Tickle tickle) {
        if(I_AM_LEADER) {
            tickleProcessor.process(tickle);
        }
    }

}
