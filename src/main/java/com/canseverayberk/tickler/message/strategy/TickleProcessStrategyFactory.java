package com.canseverayberk.tickler.message.strategy;

import com.canseverayberk.tickler.model.Tickle;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TickleProcessStrategyFactory {

    private final ApplicationContext applicationContext;

    public TickleProcessStrategy getProcessor(Tickle tickle) {
        TickleProcessStrategy strategy;
        if (Objects.nonNull(tickle.getKafkaCallbackTopic())) {
            strategy = (TickleProcessStrategy) applicationContext.getBean("tickleKafkaCallbackProcessStrategy");
        } else if (Objects.nonNull(tickle.getRestCallbackUrl())) {
            strategy = (TickleProcessStrategy) applicationContext.getBean("tickleRestCallbackProcessStrategy");
        } else {
            strategy = (TickleProcessStrategy) applicationContext.getBean("tickleLogStrategy");
        }
        strategy.setTickle(tickle);
        return strategy;
    }

}
