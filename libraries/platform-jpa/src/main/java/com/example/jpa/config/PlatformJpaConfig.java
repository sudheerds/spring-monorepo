package com.example.jpa.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PlatformJpaConfig {

    @Bean
    public static BeanFactoryPostProcessor databaseAutoCreator() {
        return new DatabaseAutoCreator();
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            try {
                return Optional.ofNullable(SecurityContextHolder.getContext())
                        .map(SecurityContext::getAuthentication)
                        .filter(Authentication::isAuthenticated)
                        .map(Authentication::getName)
                        .or(() -> Optional.of("system"));
            } catch (NoClassDefFoundError | Exception e) {
                return Optional.of("system");
            }
        };
    }
}
