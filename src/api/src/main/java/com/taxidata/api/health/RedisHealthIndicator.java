package com.taxidata.api.health;

import com.taxidata.api.service.TaxiRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHealthIndicator implements HealthIndicator {

    private final TaxiRideService taxiRideService;

    @Override
    public Health health() {
        boolean isRedisAvailable = taxiRideService.isRedisAvailable();

        if (isRedisAvailable) {
            return Health.up()
                    .withDetail("service", "Redis")
                    .withDetail("status", "Available")
                    .build();
        }

        return Health.down()
                .withDetail("service", "Redis")
                .withDetail("status", "Unavailable")
                .build();
    }
}