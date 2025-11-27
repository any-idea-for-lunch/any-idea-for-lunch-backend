package com.anyidea.lunch.dto;

public record PlaceSearchRequest(
        String keyword,
        double lat,
        double lng
) {
}
