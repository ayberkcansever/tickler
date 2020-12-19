package com.canseverayberk.tickler.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CallbackPayload {
    private Long id;
    private String name;
}
