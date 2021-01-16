package com.canseverayberk.tickler.message.strategy;

import com.canseverayberk.tickler.model.Tickle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Setter
@Component
@Scope("prototype")
public class TickleKafkaCallbackProcessStrategy implements TickleProcessStrategy {

    private final KafkaTemplate kafkaTemplate;
    private Tickle tickle;

    @Override
    public void process() {
        kafkaTemplate.send(tickle.getKafkaCallbackTopic(), tickle.getPayload());
    }
}
