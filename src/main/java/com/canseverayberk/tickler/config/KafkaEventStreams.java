package com.canseverayberk.tickler.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaEventStreams {

    String tickleInput = "tickleInput";

    @Input
    MessageChannel tickleInput();

    @Output
    MessageChannel tickleOutput();

}
