package com.canseverayberk.tickler.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface ITKafkaEventStreams {

    String callbackInput = "callbackInput";

    @Input
    MessageChannel callbackInput();

}
