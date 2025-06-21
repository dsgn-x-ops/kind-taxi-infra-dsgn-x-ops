package com.taxidata.processor.service;

import com.taxidata.processor.model.TaxiRide;
import com.taxidata.processor.repository.TaxiRideRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxiRideProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TaxiRideProcessor.class);

    private final TaxiRideRepository repository;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @PostConstruct
    public void init() {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("saveRideCB");
    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void processRide(TaxiRide ride) {
        circuitBreaker.executeRunnable(() -> {
            repository.save(ride);
            logger.info("✔ Ride saved: {} → {}",
                    ride.getStart().getPlace(),
                    ride.getEnd().getPlace());
        });
    }
}