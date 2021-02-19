package com.canseverayberk.tickler.expiry.couchbase;

import com.canseverayberk.tickler.configuration.CouchbaseConfiguration;
import com.canseverayberk.tickler.expiry.TickleSaver;
import com.canseverayberk.tickler.model.Tickle;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.kv.UpsertOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(value = CouchbaseConfiguration.class)
@Component
public class CouchbaseTickleSaver implements TickleSaver {

    private final Bucket bucket;

    @Override
    public void save(Tickle tickle) {
        try {
            String messageUUID = UUID.randomUUID().toString();
            UpsertOptions upsertOptions = UpsertOptions.upsertOptions()
                    .expiry(Duration.ofSeconds(tickle.getPureTtl()));
            bucket.defaultCollection().upsert(messageUUID, tickle, upsertOptions);
            log.info("Tickle saved for expiry: {}", tickle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
