package com.lkyl.aidemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Main data structure expected by your WeatherAgent
public record WeatherData(
    String city,
    double temperature,
    String condition,
    boolean isRaining,
    double windSpeed
) {}

