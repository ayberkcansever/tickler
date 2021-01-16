package com.canseverayberk.tickler.message.strategy;

import com.canseverayberk.tickler.model.Tickle;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Setter
@Component
@Scope("prototype")
public class TickleLogStrategy implements TickleProcessStrategy {

    private Tickle tickle;

    @Override
    public void process() {

    }
}
