package com.sahibinden.service;

import com.sahibinden.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    
    private static final String WTTR_API_URL = "https://wttr.in/{city}?format=j1";
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; WeatherApp/1.0)";
    
    private final RestTemplate restTemplate;
    
    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Gets current weather information for the specified city
     * 
     * @param city the name of the city
     * @return WeatherResponse containing weather information
     * @throws RuntimeException if the API call fails
     */
    public WeatherResponse getCurrentWeather(String city) {
        try {
            logger.info("Fetching weather data for city: {}", city);
            
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City name cannot be null or empty");
            }
            
            String sanitizedCity = city.trim().replaceAll("[^a-zA-Z0-9\\s,-]", "");
            
            WeatherResponse response = restTemplate.getForObject(
                WTTR_API_URL, 
                WeatherResponse.class, 
                sanitizedCity
            );
            
            if (response == null) {
                throw new RuntimeException("No weather data received for city: " + city);
            }
            
            logger.info("Successfully fetched weather data for city: {}", city);
            return response;
            
        } catch (HttpClientErrorException e) {
            logger.error("Client error when fetching weather for city: {} - Status: {} - Response: {}", 
                        city, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error when fetching weather for city: {} - Status: {} - Response: {}", 
                        city, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Weather service temporarily unavailable", e);
        } catch (Exception e) {
            logger.error("Unexpected error when fetching weather for city: {}", city, e);
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets weather forecast for multiple days for the specified city
     * 
     * @param city the name of the city
     * @param days number of days to forecast (1-7)
     * @return WeatherResponse containing weather information
     * @throws RuntimeException if the API call fails
     */
    public WeatherResponse getWeatherForecast(String city, int days) {
        try {
            logger.info("Fetching weather forecast for city: {} for {} days", city, days);
            
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City name cannot be null or empty");
            }
            
            if (days < 1 || days > 7) {
                throw new IllegalArgumentException("Number of days must be between 1 and 7");
            }
            
            String sanitizedCity = city.trim().replaceAll("[^a-zA-Z0-9\\s,-]", "");
            String forecastUrl = "https://wttr.in/{city}?format=j1&days=" + days;
            
            WeatherResponse response = restTemplate.getForObject(
                forecastUrl, 
                WeatherResponse.class, 
                sanitizedCity
            );
            
            if (response == null) {
                throw new RuntimeException("No weather forecast data received for city: " + city);
            }
            
            logger.info("Successfully fetched weather forecast for city: {}", city);
            return response;
            
        } catch (HttpClientErrorException e) {
            logger.error("Client error when fetching weather forecast for city: {} - Status: {} - Response: {}", 
                        city, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch weather forecast: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error when fetching weather forecast for city: {} - Status: {} - Response: {}", 
                        city, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Weather forecast service temporarily unavailable", e);
        } catch (Exception e) {
            logger.error("Unexpected error when fetching weather forecast for city: {}", city, e);
            throw new RuntimeException("Failed to fetch weather forecast: " + e.getMessage(), e);
        }
    }
}
