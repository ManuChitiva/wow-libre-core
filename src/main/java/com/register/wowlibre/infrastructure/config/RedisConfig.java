package com.register.wowlibre.infrastructure.config;

//@Configuration
//@EnableCaching
public class RedisConfig {
/*
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        // Configura la conexión de Redis con la dirección y el puerto
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("clustercfg.wowlibre.tkfyma.use2.cache.amazonaws.com"); // Cambia la IP según tus necesidades
        config.setPort(6379); // Cambia el puerto si es diferente

        return new JedisConnectionFactory(config);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Configuración general de cache
        RedisCacheConfiguration defaultCacheConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(2)); // TTL predeterminado

        // Map de configuraciones para caches específicos
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("findByUserId", configurationTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("mails", configurationTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("emailCodeCache", configurationTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("verifyAccount", configurationTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("recoveryPassword", configurationTtl(Duration.ofMinutes(2)));
        cacheConfigurations.put("realm-apikey", configurationTtl(Duration.ofHours(2)));


        // Construcción del RedisCacheManager con configuraciones específicas
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private RedisCacheConfiguration configurationTtl(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(duration);
    }

 */
}
