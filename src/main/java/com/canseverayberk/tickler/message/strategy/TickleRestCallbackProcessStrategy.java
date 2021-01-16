package com.canseverayberk.tickler.message.strategy;

import com.canseverayberk.tickler.model.Tickle;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Setter
@Component
@Scope("prototype")
public class TickleRestCallbackProcessStrategy implements TickleProcessStrategy {

    private final RestTemplate restTemplate = new RestTemplate();
    private Tickle tickle;

    @Override
    @Async("asyncTickleProcessorThreadPool")
    public void process() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(tickle.getRestCallbackUrl(),
                HttpMethod.POST,
                new HttpEntity<>(tickle.getPayload(), headers),
                Void.class);
        log.info("Callback url called for tickle: {}", tickle);
    }
}
