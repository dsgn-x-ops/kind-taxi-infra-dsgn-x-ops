package com.taxidata.api.controller;

import com.taxidata.api.dto.PageDTO;
import com.taxidata.api.dto.TaxiRideDTO;
import com.taxidata.api.service.TaxiRideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.constraints.Min;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
@Tag(name = "Taxi Rides", description = "API for querying taxi ride data")
@Validated
public class TaxiRideController {
    private static final Logger logger = LoggerFactory.getLogger(TaxiRideController.class);
    private final TaxiRideService taxiRideService;

    @GetMapping
    @Operation(summary = "Get taxi rides filtered by price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rides"),
            @ApiResponse(responseCode = "400", description = "Invalid price range parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Timed(value = "taxi.rides.search", description = "Time taken to search taxi rides")
    public ResponseEntity<PageDTO<TaxiRideDTO>> getRides(
            @Parameter(description = "Minimum price")
            @RequestParam(required = false)
            @Min(value = 0, message = "Minimum price cannot be negative")
            Double minPrice,

            @Parameter(description = "Maximum price")
            @RequestParam(required = false)
            @Min(value = 0, message = "Maximum price cannot be negative")
            Double maxPrice,

            Pageable pageable) {

        logger.debug("Searching rides with price range: {} - {}, page: {}",
                minPrice, maxPrice, pageable.getPageNumber());

        // Validate price range if both parameters are provided
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            return ResponseEntity.badRequest().build();
        }

        try {
            PageDTO<TaxiRideDTO> rides = taxiRideService.findByPriceRange(
                    minPrice != null ? minPrice : 0.0,
                    maxPrice != null ? maxPrice : Double.MAX_VALUE,
                    pageable
            );

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
                    .body(rides);

        } catch (Exception e) {
            logger.error("Error retrieving rides", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get taxi ride by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the ride"),
            @ApiResponse(responseCode = "404", description = "Ride not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Timed(value = "taxi.rides.get", description = "Time taken to get a taxi ride by ID")
    public ResponseEntity<TaxiRideDTO> getRideById(
            @Parameter(description = "Ride ID")
            @PathVariable
            @Min(value = 1, message = "ID must be positive")
            Long id) {

        logger.debug("Fetching ride with ID: {}", id);

        try {
            TaxiRideDTO ride = taxiRideService.findById(id);
            if (ride == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
                    .body(ride);

        } catch (Exception e) {
            logger.error("Error retrieving ride with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cache/status")
    @Operation(summary = "Check Redis cache status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cache status retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error checking cache status")
    })
    public ResponseEntity<String> getCacheStatus() {
        boolean isCacheAvailable = taxiRideService.isRedisAvailable();
        return ResponseEntity.ok(isCacheAvailable ? "Cache is available" : "Cache is unavailable");
    }
}