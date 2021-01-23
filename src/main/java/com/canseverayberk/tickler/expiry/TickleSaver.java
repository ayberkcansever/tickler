package com.canseverayberk.tickler.expiry;

import com.canseverayberk.tickler.model.Tickle;

public interface TickleSaver {
    void save(Tickle tickle);
}
