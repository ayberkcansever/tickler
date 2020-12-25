package com.canseverayberk.tickler.message;

import com.canseverayberk.tickler.model.Tickle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TickleProcessor {

    private final RestTemplate restTemplate = new RestTemplate();

    @Bean(name = "asyncPickleProcessorThreadPool")
    public ThreadPoolTaskExecutor asyncPickleProcessorThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("pickleProcessorThread-");
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Async("asyncPickleProcessorThreadPool")
    public void process(Tickle tickle) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(tickle.getRestCallbackUrl(),
                HttpMethod.POST,
                new HttpEntity<>(tickle.getPayload(), headers),
                Void.class);
        log.info("Callback url called for tickle: {}", tickle);
    }

}
