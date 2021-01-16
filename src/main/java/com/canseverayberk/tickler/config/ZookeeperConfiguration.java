package com.canseverayberk.tickler.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;

import static com.canseverayberk.tickler.TicklerApplication.I_AM_LEADER;

@Slf4j
@Configuration
public class ZookeeperConfiguration {

    @Bean
    public LeaderInitiatorFactoryBean leaderInitiator(CuratorFramework client) {
        return new LeaderInitiatorFactoryBean()
                .setClient(client)
                .setPath("/tickle/")
                .setRole("cluster");
    }

    @EventListener(OnGrantedEvent.class)
    public void onGranted() {
        I_AM_LEADER = true;
        log.info("I'm leader now.");
    }

    @EventListener(OnRevokedEvent.class)
    public void onRevoked() {
        I_AM_LEADER = false;
        log.info("I'm not leader anymore.");
    }

}
