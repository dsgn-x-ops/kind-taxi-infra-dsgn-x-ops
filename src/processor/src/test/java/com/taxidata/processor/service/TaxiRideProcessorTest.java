package com.taxidata.processor.service;

import com.taxidata.processor.model.Location;
import com.taxidata.processor.model.TaxiRide;
import com.taxidata.processor.repository.TaxiRideRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaxiRideProcessorTest {

    @Mock
    private TaxiRideRepository repository;

    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Mock
    private CircuitBreaker circuitBreaker;

    @InjectMocks
    private TaxiRideProcessor processor;

    @BeforeEach
    void setUp() {
        when(circuitBreakerRegistry.circuitBreaker("saveRideCB")).thenReturn(circuitBreaker);

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(circuitBreaker).executeRunnable(any());

        processor.init();
    }

    @Test
    void testProcessRide_savesTaxiRideToRepository() {
        System.out.println("Test started: processRide() should save the ride");

        Location start = new Location(40.7128, -74.0060, "New York City");
        Location end = new Location(40.7580, -73.9855, "Times Square");
        Location important = new Location(40.7527, -73.9772, "Grand Central Terminal");

        TaxiRide ride = TaxiRide.builder()
                .start(start)
                .end(end)
                .importantPlaces(List.of(important))
                .startDate(LocalDateTime.of(2024, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2024, 1, 1, 10, 30))
                .price(25.50)
                .distanceKm(3.4)
                .build();

        System.out.println("Ride created: " + ride);

        processor.processRide(ride);
        System.out.println("processRide() executed");

        verify(repository, times(1)).save(ride);
        System.out.println("Repository.save verified");
    }
}