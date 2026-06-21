package com.lkyl.aidemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// DTOs for Open-Meteo Weather Response
public record OpenMeteoResponse(
    @JsonProperty("current") CurrentData current
) {}
