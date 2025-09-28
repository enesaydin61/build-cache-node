package com.sahibinden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simplified weather information for API responses")
public class WeatherSummaryDto {
    
    @Schema(description = "City name", example = "Istanbul")
    private String city;
    
    @Schema(description = "Country name", example = "Turkey")
    private String country;
    
    @Schema(description = "Temperature in Celsius", example = "22")
    private String temperatureC;
    
    @Schema(description = "Temperature in Fahrenheit", example = "72")
    private String temperatureF;
    
    @Schema(description = "Weather description", example = "Partly cloudy")
    private String description;
    
    @Schema(description = "Humidity percentage", example = "65")
    private String humidity;
    
    @Schema(description = "Wind speed in km/h", example = "12")
    private String windSpeed;
    
    @Schema(description = "Wind direction", example = "NW")
    private String windDirection;
    
    @Schema(description = "Feels like temperature in Celsius", example = "24")
    private String feelsLikeC;
    
    @Schema(description = "Visibility in kilometers", example = "10")
    private String visibility;
    
    @Schema(description = "Pressure in hPa", example = "1013")
    private String pressure;
}
