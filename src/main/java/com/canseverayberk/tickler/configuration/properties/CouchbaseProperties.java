package com.canseverayberk.tickler.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "expiry.background", havingValue = "couchbase")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "couchbase")
@Getter
@Setter
public class CouchbaseProperties {
    private String host;
    private String username;
    private String password;
    private String bucketName;
}