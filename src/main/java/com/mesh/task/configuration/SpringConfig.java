package com.mesh.task.configuration;

import org.springframework.cache.*;
import org.springframework.cache.annotation.*;
import org.springframework.cache.concurrent.*;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableCaching
@EnableScheduling
public class SpringConfig {

    @Bean
    public CacheManager userCache() {
        return new ConcurrentMapCacheManager("userCache");
    }
}


