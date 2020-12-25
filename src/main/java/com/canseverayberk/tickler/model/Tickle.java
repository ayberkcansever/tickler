package com.canseverayberk.tickler.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tickle {

    public static final String REDIS_VALUE_SUFFIX = "_value";

    private String payload;
    private String restCallbackUrl;
    private Integer ttl;
}
