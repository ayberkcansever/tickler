package com.canseverayberk.tickler.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = KafkaEventStreams.class)
public class EventStreamsConfiguration {
}
