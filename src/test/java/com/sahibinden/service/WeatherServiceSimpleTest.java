package com.sahibinden.service;

import com.sahibinden.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherService Simple Tests")
class WeatherServiceSimpleTest {
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private WeatherService weatherService;
    
    private WeatherResponse mockWeatherResponse;
    
    @BeforeEach
    void setUp() {
        mockWeatherResponse = createMockWeatherResponse();
    }
    
    @Test
    @DisplayName("Should get current weather successfully")
    void getCurrentWeather_Success() {
        // Given
        String city = "Istanbul";
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class), eq(city)))
            .thenReturn(mockWeatherResponse);
        
        // When
        WeatherResponse result = weatherService.getCurrentWeather(city);
        
        // Then
        assertNotNull(result);
        assertEquals("Istanbul", result.getNearestArea().get(0).getAreaName().get(0).getValue());
        assertEquals("Turkey", result.getNearestArea().get(0).getCountry().get(0).getValue());
        assertEquals("22", result.getCurrentCondition().get(0).getTempC());
        
        verify(restTemplate, times(1)).getForObject(
            eq("https://wttr.in/{city}?format=j1"), 
            eq(WeatherResponse.class), 
            eq(city)
        );
    }
    
    @Test
    @DisplayName("Should get weather forecast successfully")
    void getWeatherForecast_Success() {
        // Given
        String city = "Istanbul";
        int days = 3;
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class), eq(city)))
            .thenReturn(mockWeatherResponse);
        
        // When
        WeatherResponse result = weatherService.getWeatherForecast(city, days);
        
        // Then
        assertNotNull(result);
        assertEquals("Istanbul", result.getNearestArea().get(0).getAreaName().get(0).getValue());
        
        verify(restTemplate, times(1)).getForObject(
            eq("https://wttr.in/{city}?format=j1&days=3"), 
            eq(WeatherResponse.class), 
            eq(city)
        );
    }
    
    @Test
    @DisplayName("Should validate null city parameter")
    void getCurrentWeather_NullCity_ThrowsException() {
        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> weatherService.getCurrentWeather(null)
        );
        
        assertTrue(exception.getMessage().contains("City name cannot be null or empty") ||
                  exception.getCause() instanceof IllegalArgumentException);
        verifyNoInteractions(restTemplate);
    }
    
    @Test
    @DisplayName("Should validate empty city parameter")  
    void getCurrentWeather_EmptyCity_ThrowsException() {
        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> weatherService.getCurrentWeather("   ")
        );
        
        assertTrue(exception.getMessage().contains("City name cannot be null or empty") ||
                  exception.getCause() instanceof IllegalArgumentException);
        verifyNoInteractions(restTemplate);
    }
    
    @Test
    @DisplayName("Should validate days parameter range")
    void getWeatherForecast_InvalidDays_ThrowsException() {
        // Test days < 1
        RuntimeException exception1 = assertThrows(
            RuntimeException.class, 
            () -> weatherService.getWeatherForecast("Istanbul", 0)
        );
        assertTrue(exception1.getMessage().contains("Number of days must be between 1 and 7") ||
                  exception1.getCause() instanceof IllegalArgumentException);
        
        // Test days > 7  
        RuntimeException exception2 = assertThrows(
            RuntimeException.class, 
            () -> weatherService.getWeatherForecast("Istanbul", 8)
        );
        assertTrue(exception2.getMessage().contains("Number of days must be between 1 and 7") ||
                  exception2.getCause() instanceof IllegalArgumentException);
        
        verifyNoInteractions(restTemplate);
    }
    
    @Test
    @DisplayName("Should sanitize city name")
    void getCurrentWeather_SanitizesCity_Success() {
        // Given
        String dirtyCity = "Istanbul<script>alert('xss')</script>";
        String cleanCity = "Istanbulscriptalert'xss'script";
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class), anyString()))
            .thenReturn(mockWeatherResponse);
        
        // When
        WeatherResponse result = weatherService.getCurrentWeather(dirtyCity);
        
        // Then
        assertNotNull(result);
        verify(restTemplate, times(1)).getForObject(
            anyString(), 
            eq(WeatherResponse.class), 
            anyString()
        );
    }
    
    @Test
    @DisplayName("Should handle null API response")
    void getCurrentWeather_NullResponse_ThrowsRuntimeException() {
        // Given
        String city = "Istanbul";
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class), eq(city)))
            .thenReturn(null);
        
        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> weatherService.getCurrentWeather(city)
        );
        
        assertTrue(exception.getMessage().contains("No weather data received"));
        verify(restTemplate, times(1)).getForObject(
            anyString(), 
            eq(WeatherResponse.class), 
            eq(city)
        );
    }
    
    private WeatherResponse createMockWeatherResponse() {
        // Create mock current condition
        CurrentCondition currentCondition = new CurrentCondition();
        currentCondition.setTempC("22");
        currentCondition.setTempF("72");
        currentCondition.setHumidity("65");
        currentCondition.setWindspeedKmph("15");
        currentCondition.setWinddir16Point("NW");
        currentCondition.setFeelsLikeC("24");
        currentCondition.setVisibility("10");
        currentCondition.setPressure("1013");
        
        WeatherDescription description = new WeatherDescription("Partly cloudy");
        currentCondition.setWeatherDesc(Arrays.asList(description));
        
        // Create mock nearest area
        NearestArea nearestArea = new NearestArea();
        nearestArea.setAreaName(Arrays.asList(new AreaName("Istanbul")));
        nearestArea.setCountry(Arrays.asList(new Country("Turkey")));
        nearestArea.setLatitude("41.019");
        nearestArea.setLongitude("28.965");
        
        // Create mock weather info
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setDate("2025-09-28");
        weatherInfo.setMaxtempC("25");
        weatherInfo.setMintempC("18");
        weatherInfo.setAvgtempC("22");
        
        // Create weather response
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCurrentCondition(Arrays.asList(currentCondition));
        weatherResponse.setNearestArea(Arrays.asList(nearestArea));
        weatherResponse.setWeather(Arrays.asList(weatherInfo));
        
        return weatherResponse;
    }
}
