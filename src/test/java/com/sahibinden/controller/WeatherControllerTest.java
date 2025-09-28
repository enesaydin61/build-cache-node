package com.sahibinden.controller;

import com.sahibinden.dto.WeatherSummaryDto;
import com.sahibinden.model.*;
import com.sahibinden.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
@DisplayName("WeatherController Integration Tests")
class WeatherControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private WeatherService weatherService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private WeatherResponse mockWeatherResponse;
    
    @BeforeEach
    void setUp() {
        mockWeatherResponse = createMockWeatherResponse();
    }
    
    @Test
    @DisplayName("GET /api/weather/current/{city} - Should return weather summary successfully")
    void getCurrentWeather_Success() throws Exception {
        // Given
        String city = "Istanbul";
        when(weatherService.getCurrentWeather(city)).thenReturn(mockWeatherResponse);
        
        // When & Then
        mockMvc.perform(get("/api/weather/current/{city}", city))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.city", is("Istanbul")))
            .andExpect(jsonPath("$.country", is("Turkey")))
            .andExpect(jsonPath("$.temperatureC", is("22")))
            .andExpect(jsonPath("$.temperatureF", is("72")))
            .andExpect(jsonPath("$.description", is("Partly cloudy")))
            .andExpect(jsonPath("$.humidity", is("65")))
            .andExpect(jsonPath("$.windSpeed", is("15")))
            .andExpect(jsonPath("$.windDirection", is("NW")))
            .andExpect(jsonPath("$.feelsLikeC", is("24")))
            .andExpect(jsonPath("$.visibility", is("10")))
            .andExpect(jsonPath("$.pressure", is("1013")));
        
        verify(weatherService, times(1)).getCurrentWeather(city);
    }
    
    @Test
    @DisplayName("GET /api/weather/current/{city} - Should handle service exception")
    void getCurrentWeather_ServiceException_Returns500() throws Exception {
        // Given
        String city = "NonExistentCity";
        when(weatherService.getCurrentWeather(city))
            .thenThrow(new RuntimeException("City not found"));
        
        // When & Then
        mockMvc.perform(get("/api/weather/current/{city}", city))
            .andExpect(status().isInternalServerError());
        
        verify(weatherService, times(1)).getCurrentWeather(city);
    }
    
    @Test
    @DisplayName("GET /api/weather/current/{city} - Should handle illegal argument exception")
    void getCurrentWeather_IllegalArgumentException_Returns400() throws Exception {
        // Given
        String city = "InvalidCity";
        when(weatherService.getCurrentWeather(city))
            .thenThrow(new IllegalArgumentException("City name cannot be empty"));
        
        // When & Then
        mockMvc.perform(get("/api/weather/current/{city}", city))
            .andExpect(status().isBadRequest());
        
        verify(weatherService, times(1)).getCurrentWeather(city);
    }
    
    @Test
    @DisplayName("GET /api/weather/forecast/{city} - Should return forecast successfully")
    void getWeatherForecast_Success() throws Exception {
        // Given
        String city = "Istanbul";
        int days = 3;
        when(weatherService.getWeatherForecast(city, days)).thenReturn(mockWeatherResponse);
        
        // When & Then
        mockMvc.perform(get("/api/weather/forecast/{city}", city)
                .param("days", String.valueOf(days)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nearest_area", hasSize(1)))
            .andExpect(jsonPath("$.current_condition", hasSize(1)))
            .andExpect(jsonPath("$.weather", hasSize(1)))
            .andExpect(jsonPath("$.nearest_area[0].areaName[0].value", is("Istanbul")))
            .andExpect(jsonPath("$.current_condition[0].temp_C", is("22")));
        
        verify(weatherService, times(1)).getWeatherForecast(city, days);
    }
    
    @Test
    @DisplayName("GET /api/weather/forecast/{city} - Should use default days parameter")
    void getWeatherForecast_DefaultDays_Success() throws Exception {
        // Given
        String city = "Istanbul";
        int defaultDays = 3;
        when(weatherService.getWeatherForecast(city, defaultDays)).thenReturn(mockWeatherResponse);
        
        // When & Then
        mockMvc.perform(get("/api/weather/forecast/{city}", city))
            .andExpect(status().isOk());
        
        verify(weatherService, times(1)).getWeatherForecast(city, defaultDays);
    }
    
    @Test
    @DisplayName("GET /api/weather/forecast/{city} - Should validate days parameter")
    void getWeatherForecast_InvalidDays_Returns400() throws Exception {
        // Given
        String city = "Istanbul";
        int invalidDays = 10;
        
        // When & Then
        mockMvc.perform(get("/api/weather/forecast/{city}", city)
                .param("days", String.valueOf(invalidDays)))
            .andExpect(status().isBadRequest());
        
        verify(weatherService, never()).getWeatherForecast(anyString(), anyInt());
    }
    
    @Test
    @DisplayName("GET /api/weather/full/{city} - Should return full weather details")
    void getFullWeatherDetails_Success() throws Exception {
        // Given
        String city = "Istanbul";
        when(weatherService.getCurrentWeather(city)).thenReturn(mockWeatherResponse);
        
        // When & Then
        mockMvc.perform(get("/api/weather/full/{city}", city))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nearest_area[0].areaName[0].value", is("Istanbul")))
            .andExpect(jsonPath("$.nearest_area[0].country[0].value", is("Turkey")))
            .andExpect(jsonPath("$.current_condition[0].temp_C", is("22")))
            .andExpect(jsonPath("$.current_condition[0].temp_F", is("72")))
            .andExpect(jsonPath("$.weather[0].date", is("2025-09-28")))
            .andExpect(jsonPath("$.weather[0].maxtempC", is("25")));
        
        verify(weatherService, times(1)).getCurrentWeather(city);
    }
    
    @Test
    @DisplayName("GET /api/weather/health - Should return health status")
    void healthCheck_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/weather/health"))
            .andExpect(status().isOk())
            .andExpect(content().string("Weather Service is running!"));
        
        verifyNoInteractions(weatherService);
    }
    
    @Test
    @DisplayName("Should handle special characters in city name")
    void getCurrentWeather_SpecialCharacters_Success() throws Exception {
        // Given
        String cityWithSpaces = "New York";
        when(weatherService.getCurrentWeather(any())).thenReturn(mockWeatherResponse);
        
        // When & Then
        mockMvc.perform(get("/api/weather/current/{city}", cityWithSpaces))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.city", is("Istanbul"))); // Our mock returns Istanbul
        
        verify(weatherService, times(1)).getCurrentWeather(cityWithSpaces);
    }
    
    @Test
    @DisplayName("Should handle CORS preflight request")
    void options_CORS_Success() throws Exception {
        // When & Then
        mockMvc.perform(options("/api/weather/current/Istanbul")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
            .andExpect(status().isOk());
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
        nearestArea.setPopulation("15000000");
        
        // Create mock weather info
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setDate("2025-09-28");
        weatherInfo.setMaxtempC("25");
        weatherInfo.setMintempC("18");
        weatherInfo.setAvgtempC("22");
        weatherInfo.setSunHour("8.5");
        weatherInfo.setUvIndex("3");
        
        // Create weather response
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCurrentCondition(Arrays.asList(currentCondition));
        weatherResponse.setNearestArea(Arrays.asList(nearestArea));
        weatherResponse.setWeather(Arrays.asList(weatherInfo));
        
        return weatherResponse;
    }
}
