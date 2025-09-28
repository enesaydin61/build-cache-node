package com.sahibinden.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    
    @JsonProperty("current_condition")
    private List<CurrentCondition> currentCondition;
    
    @JsonProperty("weather")
    private List<WeatherInfo> weather;
    
    @JsonProperty("nearest_area")
    private List<NearestArea> nearestArea;
}
