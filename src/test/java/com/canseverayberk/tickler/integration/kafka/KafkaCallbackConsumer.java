package com.canseverayberk.tickler.integration.kafka;

import com.canseverayberk.tickler.config.ITKafkaEventStreams;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaCallbackConsumer {

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload = null;

    @StreamListener(ITKafkaEventStreams.callbackInput)
    public void receive(String payload) {
        this.payload = payload;
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public String getPayload() {
        return payload;
    }
}