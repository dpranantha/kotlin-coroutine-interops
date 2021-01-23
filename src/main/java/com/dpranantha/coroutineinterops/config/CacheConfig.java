package com.dpranantha.coroutineinterops.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        // These are unbounded and no TTL
        final CaffeineCache basicProductCache = new CaffeineCache("BasicProduct",
                Caffeine.newBuilder()
                        .recordStats()
                        .build());
        final CaffeineCache descriptionCache = new CaffeineCache("Description",
                Caffeine.newBuilder()
                        .recordStats()
                        .build());
        final CaffeineCache sellerCache = new CaffeineCache("Seller",
                Caffeine.newBuilder()
                        .recordStats()
                        .build());
        final CaffeineCache offerCache = new CaffeineCache("Offer",
                Caffeine.newBuilder()
                        .recordStats()
                        .build());
        final CaffeineCache reviewCache = new CaffeineCache("Review",
                Caffeine.newBuilder()
                        .recordStats()
                        .build());
        final List<CaffeineCache> caches = Arrays.asList(basicProductCache,
                descriptionCache,
                sellerCache,
                offerCache,
                reviewCache);

        final SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(caches);

        return manager;
    }
}
