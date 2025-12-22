package com.example.notification.strategy;

import com.example.notification.model.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class NotificationStrategyFactory {
    private final Map<Channel, NotificationStrategy> strategies;

    public NotificationStrategyFactory(List<NotificationStrategy> strategyList) {
        this.strategies = new HashMap<>();
        for (NotificationStrategy strategy : strategyList) {
            registerStrategy(strategy);
        }
    }

    /**
     * Register a notification strategy
     * @param strategy Strategy to register
     */
    public void registerStrategy(NotificationStrategy strategy) {
        strategies.put(strategy.getChannel(), strategy);
    }

    /**
     * Get strategy for a specific channel
     * @param channel Channel type
     * @return NotificationStrategy for the channel
     * @throws IllegalArgumentException if no strategy found for the channel
     */
    public NotificationStrategy getStrategy(Channel channel) {
        NotificationStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for channel: " + channel);
        }
        return strategy;
    }

    /**
     * Get all registered channels
     * @return Set of supported channels
     */
    public Set<Channel> getSupportedChannels() {
        return strategies.keySet();
    }
}
