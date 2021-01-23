package com.canseverayberk.tickler.configuration;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(value = ITKafkaEventStreams.class)
public class ITEventStreamsConfiguration {

}
