package com.canseverayberk.tickler.expiry;

import com.canseverayberk.tickler.configuration.KafkaEventStreams;
import com.canseverayberk.tickler.model.Tickle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class TickleExpirationHandler {

    @Autowired
    private KafkaEventStreams kafkaEventStreams;

    protected void handleExpiredTickle(Tickle tickle) {
        kafkaEventStreams.tickleProcessOutput().send(MessageBuilder.withPayload(tickle).build());
    }
}
