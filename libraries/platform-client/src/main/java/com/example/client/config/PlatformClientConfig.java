package com.example.client.config;

import com.example.client.interceptor.BearerTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class PlatformClientConfig {

    @Bean
    @Primary
    public RestClient.Builder restClientBuilder() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofSeconds(10).toMillis());

        RestClient.Builder builder = RestClient.builder()
                .requestFactory(requestFactory);

        // Conditionally add security interceptor if Spring Security is present
        try {
            Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            builder.requestInterceptor(new BearerTokenInterceptor());
        } catch (ClassNotFoundException e) {
            // Spring Security is not on classpath, skip adding token propagation
        }

        return builder;
    }
}
