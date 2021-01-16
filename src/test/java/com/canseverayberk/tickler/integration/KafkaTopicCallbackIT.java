package com.canseverayberk.tickler.integration;

import com.canseverayberk.tickler.AbstractIT;
import com.canseverayberk.tickler.integration.kafka.KafkaCallbackConsumer;
import com.canseverayberk.tickler.model.CallbackPayload;
import com.canseverayberk.tickler.model.Tickle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaTopicCallbackIT extends AbstractIT {

    @Autowired
    private KafkaCallbackConsumer kafkaCallbackConsumer;

    @Test
    void should_produce_to_kafka_topic_when_tickle_expires() throws InterruptedException, JsonProcessingException {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(CallbackPayload.builder().id(1L).name("name").build());

        Tickle tickle = Tickle.builder()
                .payload(payload)
                .kafkaCallbackTopic("tickle.integration.test")
                .ttl(3)
                .build();

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.exchange("http://localhost:19090/api/v1/tickle",
                HttpMethod.POST,
                new HttpEntity<>(tickle, headers),
                Void.class);

        // then
        assertThat(kafkaCallbackConsumer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();
        assertThat(kafkaCallbackConsumer.getPayload()).isEqualTo(objectMapper.writeValueAsString(payload));
    }

}
