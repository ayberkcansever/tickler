package com.canseverayberk.tickler.expiry.redis;

import com.canseverayberk.tickler.configuration.RedisConfiguration;
import com.canseverayberk.tickler.expiry.TickleSaver;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.canseverayberk.tickler.model.Tickle.REDIS_VALUE_SUFFIX;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(value = RedisConfiguration.class)
@Component
public class RedisTickleSaver implements TickleSaver {

    private final RedisTemplate<String, Tickle> redisTemplate;

    @Override
    public void save(Tickle tickle) {
        String messageUUID = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(messageUUID, tickle, tickle.getTtl(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(messageUUID.concat(REDIS_VALUE_SUFFIX), tickle, tickle.getPureTtl() + 20, TimeUnit.SECONDS);
        log.info("Tickle saved for expiry: {}", tickle);
    }
}
