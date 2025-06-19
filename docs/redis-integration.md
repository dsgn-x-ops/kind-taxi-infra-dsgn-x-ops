# Redis Integration – API Service

**Purpose:**

Implement Redis caching in the API service to improve performance by reducing redundant access to the PostgreSQL database. This setup allows frequently accessed data (e.g., ride searches) to be retrieved directly from memory, drastically improving response time.

---

## Configuration Summary

### RedisConfig.java

This configuration includes:

- **Connection pooling** using `LettuceConnectionFactory`
- **Value serialization** with `GenericJackson2JsonRedisSerializer`
- **Time-To-Live (TTL)** of 30 minutes for all cache entries
- **Graceful error handling** via `SimpleCacheErrorHandler`
- **Global cache manager** using `RedisCacheManager`

### Dependencies (`build.gradle.kts`)

```kotlin
implementation("org.springframework.boot:spring-boot-starter-cache")
implementation("org.springframework.boot:spring-boot-starter-data-redis")
```