package com.canseverayberk.tickler.model;

import com.canseverayberk.tickler.controller.validation.ValidTickleCallback;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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

    @NotNull(message = "time-to-live value must be provided!")
    @Min(value = 1, message = "time-to-live value must be greater than 1!")
    private Integer ttl;

    public boolean valid() {
        return callbackValid() && Objects.nonNull(payload);
    }

    private boolean callbackValid() {
        return Objects.nonNull(restCallbackUrl) || Objects.nonNull(kafkaCallbackTopic);
    }
}
