package com.lkyl.aidemo.model;

import java.util.List;

// DTOs for Open-Meteo Geocoding Response
public record GeocodingResponse(List<GeocodingResult> results) {}
