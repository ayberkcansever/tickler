package com.canseverayberk.tickler.model;

import com.canseverayberk.tickler.request.rest.validation.ValidTickle;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ValidTickle
public class Tickle {

    public static final String REDIS_VALUE_SUFFIX = "_value";

    private String payload;
    private String restCallbackUrl;
    private String kafkaCallbackTopic;
    private Long ttl;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expirationDate;

    public Long getPureTtl() {
        return Objects.isNull(ttl) ? LocalDateTime.now().until(expirationDate, ChronoUnit.SECONDS) : ttl;
    }

    public boolean valid() {
        return Objects.nonNull(payload) && callbackValid() && expirationValid();
    }

    private boolean callbackValid() {
        return Objects.nonNull(restCallbackUrl) || Objects.nonNull(kafkaCallbackTopic);
    }

    private boolean expirationValid() {
        return Objects.nonNull(ttl) || Objects.nonNull(expirationDate);
    }
}
