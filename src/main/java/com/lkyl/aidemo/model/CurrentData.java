package com.lkyl.aidemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentData(
    @JsonProperty("temperature_2m") double temperature2m,
    @JsonProperty("weather_code") int weatherCode,
    @JsonProperty("wind_speed_10m") double windSpeed10m
) {}
