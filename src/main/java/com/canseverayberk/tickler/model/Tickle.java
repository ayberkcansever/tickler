package com.canseverayberk.tickler.model;

import com.canseverayberk.tickler.controller.validation.ValidTickleCallback;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ValidTickleCallback
public class Tickle {

    public static final String REDIS_VALUE_SUFFIX = "_value";

    @NotEmpty(message = "Payload must be provided!")
    private String payload;

    private String restCallbackUrl;
    private String kafkaCallbackTopic;

    @NotNull(message = "Time-to-live value must be provided!")
    private Integer ttl;
}
