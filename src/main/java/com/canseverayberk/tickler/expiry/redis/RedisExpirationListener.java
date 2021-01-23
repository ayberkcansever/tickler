package com.canseverayberk.tickler.expiry.redis;

import com.canseverayberk.tickler.configuration.RedisConfiguration;
import com.canseverayberk.tickler.expiry.TickleExpirationHandler;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.canseverayberk.tickler.TicklerApplication.I_AM_LEADER;
import static com.canseverayberk.tickler.model.Tickle.REDIS_VALUE_SUFFIX;

@Slf4j
@Component
@ConditionalOnBean(value = RedisConfiguration.class)
@RequiredArgsConstructor
public class RedisExpirationListener extends TickleExpirationHandler implements MessageListener {

    private final RedisTemplate<String, Tickle> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // only the leader should process expired key message
        if (!I_AM_LEADER) {
            return;
        }
        String key = new String(message.getBody());
        Tickle expiredTickle = redisTemplate.opsForValue().get(key.concat(REDIS_VALUE_SUFFIX));
        if (Objects.nonNull(expiredTickle)) {
            log.info("Tickle expired: {}", expiredTickle);
            handleExpiredTickle(expiredTickle);
        }
    }

}