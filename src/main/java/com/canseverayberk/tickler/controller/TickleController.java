package com.canseverayberk.tickler.controller;

import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.canseverayberk.tickler.model.Tickle.REDIS_VALUE_SUFFIX;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickle")
public class TickleController {

    private final RedisTemplate<String, Tickle> redisTemplate;

    @PostMapping
    public void tickle(@Valid @RequestBody Tickle tickle) {
        log.info("Tickle requested: {}", tickle);
        String messageUUID = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(messageUUID, tickle, tickle.getTtl(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(messageUUID.concat(REDIS_VALUE_SUFFIX), tickle, tickle.getTtl() + 20, TimeUnit.SECONDS);
    }

}
