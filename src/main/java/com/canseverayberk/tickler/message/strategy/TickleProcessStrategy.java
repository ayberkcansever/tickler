package com.canseverayberk.tickler.message.strategy;

import com.canseverayberk.tickler.model.Tickle;

public interface TickleProcessStrategy {
    void setTickle(Tickle tickle);
    void process();
}
