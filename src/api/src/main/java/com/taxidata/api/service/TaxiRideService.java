package com.taxidata.api.service;

import com.taxidata.api.dto.PageDTO;
import com.taxidata.api.dto.TaxiRideDTO;
import com.taxidata.api.model.TaxiRide;
import com.taxidata.api.repository.TaxiRideRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TaxiRideService {
    private static final Logger logger = LoggerFactory.getLogger(TaxiRideService.class);
    private static final String CACHE_NAME = "rides";

    private final TaxiRideRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = CACHE_NAME, key = "'price_range:' + #minPrice + '_' + #maxPrice", unless = "#result.empty")
    public PageDTO<TaxiRideDTO> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        try {
            logger.debug("Fetching rides from database for price range: {} - {}, page: {}", minPrice, maxPrice, pageable.getPageNumber());
            Page<TaxiRide> page = repository.findByPriceRange(minPrice, maxPrice, pageable);
            return PageDTO.fromPage(page, TaxiRideDTO::fromEntity);
        } catch (SerializationException e) {
            logger.error("Error deserializing cached data: {}", e.getMessage());
            return new PageDTO<>();
        }
    }

    @Cacheable(value = CACHE_NAME, key = "'ride:' + #id", unless = "#result == null")
    public TaxiRideDTO findById(Long id) {
        logger.debug("Fetching ride from database with ID: {}", id);
        return repository.findById(id)
                .map(TaxiRideDTO::fromEntity)
                .orElse(null);
    }

    public boolean isRedisAvailable() {
        try {
            String key = "health:check";
            redisTemplate.opsForValue().set(key, "ok", 1, TimeUnit.SECONDS);
            String result = (String) redisTemplate.opsForValue().get(key);
            return "ok".equals(result);
        } catch (Exception e) {
            logger.error("Redis health check failed: {}", e.getMessage());
            return false;
        }
    }
}