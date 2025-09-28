package com.sahibinden.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearestArea {
    
    @JsonProperty("areaName")
    private List<AreaName> areaName;
    
    @JsonProperty("country")
    private List<Country> country;
    
    @JsonProperty("region")
    private List<Region> region;
    
    @JsonProperty("latitude")
    private String latitude;
    
    @JsonProperty("longitude")
    private String longitude;
    
    @JsonProperty("population")
    private String population;
    
    @JsonProperty("weatherUrl")
    private List<WeatherUrl> weatherUrl;
}
