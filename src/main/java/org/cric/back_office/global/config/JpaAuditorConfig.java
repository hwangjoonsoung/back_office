package org.cric.back_office.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class JpaAuditorConfig {

    @Bean
    public AuditorAware<String> stringAuditorAware() {
        return () -> Optional.of(UUID.randomUUID().toString().substring(0, 6));
    }
}
