package com.build.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Add custom headers for wttr.in API
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().add("User-Agent", "Mozilla/5.0 (compatible; WeatherApp/1.0)");
            request.getHeaders().add("Accept", "application/json");
            return execution.execute(request, body);
        };
        
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }
}
