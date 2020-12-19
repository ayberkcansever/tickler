package com.canseverayberk.tickler.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tickle {
    private String payload;
    private String restCallbackUrl;
    private Integer ttl;
}
