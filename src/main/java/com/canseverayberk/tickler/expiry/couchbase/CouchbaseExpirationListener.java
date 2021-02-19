package com.canseverayberk.tickler.expiry.couchbase;

import com.canseverayberk.tickler.expiry.TickleExpirationHandler;
import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickle/couchbase")
public class CouchbaseExpirationListener extends TickleExpirationHandler {

    @PostMapping("/expire")
    public void tickleExpired(@RequestBody Tickle tickle) {
        log.info("Tickle expired: {}", tickle);
        handleExpiredTickle(tickle);
    }
}
