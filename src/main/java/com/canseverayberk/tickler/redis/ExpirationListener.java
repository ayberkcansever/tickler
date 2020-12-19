package com.canseverayberk.tickler.redis;

import com.canseverayberk.tickler.config.KafkaEventStreams;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpirationListener implements MessageListener {

    private final RedisTemplate<String, Tickle> redisTemplate;
    private final KafkaEventStreams kafkaEventStreams;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody());
        Tickle expiredTickle = redisTemplate.opsForValue().get(key.concat("_value"));
        if (Objects.nonNull(expiredTickle)) {
            kafkaEventStreams.tickleOutput().send(MessageBuilder.withPayload(expiredTickle).build());
        }
    }
}