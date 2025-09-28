package com.build.controller;

import com.build.dto.WeatherSummaryDto;
import com.build.model.CurrentCondition;
import com.build.model.NearestArea;
import com.build.model.WeatherResponse;
import com.build.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather API", description = "Hava durumu bilgilerini sağlayan REST API servisi")
@CrossOrigin(origins = "*")
public class WeatherController {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    
    private final WeatherService weatherService;
    
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    
    @GetMapping("/current/{city}")
    @Operation(
        summary = "Mevcut hava durumu bilgisini getirir",
        description = "Belirtilen şehir için güncel hava durumu bilgilerini wttr.in API'sinden alır"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Başarılı işlem"),
        @ApiResponse(responseCode = "400", description = "Geçersiz şehir adı"),
        @ApiResponse(responseCode = "404", description = "Şehir bulunamadı"),
        @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<WeatherSummaryDto> getCurrentWeather(
            @Parameter(description = "Şehir adı (örn: Istanbul, Ankara, London)", example = "Istanbul")
            @PathVariable String city) {
        
        try {
            logger.info("Weather request received for city: {}", city);
            
            WeatherResponse weatherResponse = weatherService.getCurrentWeather(city);
            WeatherSummaryDto summary = convertToSummary(weatherResponse);
            
            return ResponseEntity.ok(summary);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid city name provided: {}", city);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching weather for city: {}", city, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/forecast/{city}")
    @Operation(
        summary = "Hava durumu tahmini getirir",
        description = "Belirtilen şehir için belirtilen gün sayısında hava durumu tahminini getirir"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Başarılı işlem"),
        @ApiResponse(responseCode = "400", description = "Geçersiz parametreler"),
        @ApiResponse(responseCode = "404", description = "Şehir bulunamadı"),
        @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<WeatherResponse> getWeatherForecast(
            @Parameter(description = "Şehir adı (örn: Istanbul, Ankara, London)", example = "Istanbul")
            @PathVariable String city,
            @Parameter(description = "Tahmin günü sayısı (1-7 arası)", example = "3")
            @RequestParam(defaultValue = "3") int days) {
        
        try {
            logger.info("Weather forecast request received for city: {} for {} days", city, days);
            
            if (days < 1 || days > 7) {
                logger.warn("Invalid days parameter: {}", days);
                return ResponseEntity.badRequest().build();
            }
            
            WeatherResponse weatherResponse = weatherService.getWeatherForecast(city, days);
            
            return ResponseEntity.ok(weatherResponse);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid parameters - city: {}, days: {}", city, days);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching weather forecast for city: {} with {} days", city, days, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/full/{city}")
    @Operation(
        summary = "Detaylı hava durumu bilgisini getirir",
        description = "Belirtilen şehir için wttr.in API'sinden tam detaylı hava durumu bilgilerini getirir"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Başarılı işlem"),
        @ApiResponse(responseCode = "400", description = "Geçersiz şehir adı"),
        @ApiResponse(responseCode = "404", description = "Şehir bulunamadı"),
        @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    public ResponseEntity<WeatherResponse> getFullWeatherDetails(
            @Parameter(description = "Şehir adı (örn: Istanbul, Ankara, London)", example = "Istanbul")
            @PathVariable String city) {
        
        try {
            logger.info("Full weather details request received for city: {}", city);
            
            WeatherResponse weatherResponse = weatherService.getCurrentWeather(city);
            
            return ResponseEntity.ok(weatherResponse);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid city name provided: {}", city);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching full weather details for city: {}", city, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/health")
    @Operation(
        summary = "Servis sağlık kontrolü",
        description = "Weather API servisinin çalışıp çalışmadığını kontrol eder"
    )
    @ApiResponse(responseCode = "200", description = "Servis çalışıyor")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Weather Service is running!");
    }
    
    private WeatherSummaryDto convertToSummary(WeatherResponse weatherResponse) {
        WeatherSummaryDto summary = new WeatherSummaryDto();
        
        try {
            // Extract current condition data
            if (weatherResponse.getCurrentCondition() != null && !weatherResponse.getCurrentCondition().isEmpty()) {
                CurrentCondition current = weatherResponse.getCurrentCondition().get(0);
                summary.setTemperatureC(current.getTempC());
                summary.setTemperatureF(current.getTempF());
                summary.setHumidity(current.getHumidity());
                summary.setWindSpeed(current.getWindspeedKmph());
                summary.setWindDirection(current.getWinddir16Point());
                summary.setFeelsLikeC(current.getFeelsLikeC());
                summary.setVisibility(current.getVisibility());
                summary.setPressure(current.getPressure());
                
                if (current.getWeatherDesc() != null && !current.getWeatherDesc().isEmpty()) {
                    summary.setDescription(current.getWeatherDesc().get(0).getValue());
                }
            }
            
            // Extract location data
            if (weatherResponse.getNearestArea() != null && !weatherResponse.getNearestArea().isEmpty()) {
                NearestArea area = weatherResponse.getNearestArea().get(0);
                
                if (area.getAreaName() != null && !area.getAreaName().isEmpty()) {
                    summary.setCity(area.getAreaName().get(0).getValue());
                }
                
                if (area.getCountry() != null && !area.getCountry().isEmpty()) {
                    summary.setCountry(area.getCountry().get(0).getValue());
                }
            }
            
        } catch (Exception e) {
            logger.warn("Error converting weather response to summary", e);
            // Return partially filled summary even if some data extraction fails
        }
        
        return summary;
    }
}
