package com.canseverayberk.tickler.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KafkaEventStreams {

    String tickleRequestInput = "tickleRequestInput";
    String tickleProcessInput = "tickleProcessInput";

    @Input
    MessageChannel tickleProcessInput();

    @Output
    MessageChannel tickleProcessOutput();

    @Input
    MessageChannel tickleRequestInput();
}
