package com.build;

import com.build.controller.WeatherController;
import com.build.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("WeatherApplication Integration Tests")
class WeatherApplicationTests {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private WeatherController weatherController;
    
    @Autowired
    private WeatherService weatherService;
    
    @Test
    @DisplayName("Application context should load successfully")
    void contextLoads() {
        assertNotNull(applicationContext);
        assertNotNull(weatherController);
        assertNotNull(weatherService);
    }
    
    @Test
    @DisplayName("Health endpoint should be accessible")
    void healthEndpoint_ShouldReturnOk() {
        // Given
        String url = "http://localhost:" + port + "/api/weather/health";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Weather Service is running!", response.getBody());
    }
    
    @Test
    @DisplayName("Current weather endpoint should be accessible")
    void currentWeatherEndpoint_ShouldReturnResponse() {
        // Given
        String url = "http://localhost:" + port + "/api/weather/current/Istanbul";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        // We expect either success (200) or some error status, but not 404
        assertNotEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    @DisplayName("Forecast endpoint should be accessible")
    void forecastEndpoint_ShouldReturnResponse() {
        // Given
        String url = "http://localhost:" + port + "/api/weather/forecast/Istanbul?days=1";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        // We expect either success (200) or some error status, but not 404
        assertNotEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    @DisplayName("Full weather endpoint should be accessible")
    void fullWeatherEndpoint_ShouldReturnResponse() {
        // Given
        String url = "http://localhost:" + port + "/api/weather/full/Istanbul";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        // We expect either success (200) or some error status, but not 404
        assertNotEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    @DisplayName("Invalid endpoint should return 404")
    void invalidEndpoint_ShouldReturn404() {
        // Given
        String url = "http://localhost:" + port + "/api/weather/invalid";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
    @Test
    @DisplayName("Swagger UI should be accessible")
    void swaggerUI_ShouldBeAccessible() {
        // Given
        String url = "http://localhost:" + port + "/swagger-ui/index.html";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("swagger-ui"));
    }
    
    @Test
    @DisplayName("OpenAPI docs should be accessible")
    void openApiDocs_ShouldBeAccessible() {
        // Given
        String url = "http://localhost:" + port + "/api-docs";
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("openapi"));
    }
    
    @Test
    @DisplayName("Application should handle concurrent requests")
    void concurrentRequests_ShouldBeHandled() throws InterruptedException {
        // Given
        String url = "http://localhost:" + port + "/api/weather/health";
        final boolean[] results = new boolean[5];
        Thread[] threads = new Thread[5];
        
        // When
        for (int i = 0; i < 5; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                    results[index] = response.getStatusCode() == HttpStatus.OK;
                } catch (Exception e) {
                    results[index] = false;
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join(5000); // 5 second timeout
        }
        
        // Then
        for (boolean result : results) {
            assertTrue(result, "All concurrent requests should succeed");
        }
    }
}
