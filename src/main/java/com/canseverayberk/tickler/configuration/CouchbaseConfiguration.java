package com.canseverayberk.tickler.configuration;

import com.canseverayberk.tickler.configuration.properties.CouchbaseProperties;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(value = CouchbaseProperties.class)
public class CouchbaseConfiguration {

    private final CouchbaseProperties couchbaseProperties;

    public CouchbaseConfiguration(CouchbaseProperties couchbaseProperties) {
        this.couchbaseProperties = couchbaseProperties;
    }

    @Bean
    public Cluster cluster() {
        ClusterOptions clusterOptions = ClusterOptions
                .clusterOptions(couchbaseProperties.getUsername(), couchbaseProperties.getPassword());
        return Cluster.connect(couchbaseProperties.getHost(), clusterOptions);
    }

    @Bean
    public Bucket bucket(Cluster cluster) {
        return cluster.bucket(couchbaseProperties.getBucketName());
    }
}