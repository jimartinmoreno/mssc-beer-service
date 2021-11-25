package guru.springframework.msscbeerservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @EnableCaching Enables Spring's annotation-driven cache management capability, similar to the support found in
 * Spring's <cache:*> XML namespace. To be used together with @Configuration classes as follows:
 */
@EnableCaching
@Configuration
public class CacheConfig {
}
