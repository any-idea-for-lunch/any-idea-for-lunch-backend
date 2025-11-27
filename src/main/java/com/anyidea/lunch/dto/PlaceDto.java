package com.anyidea.lunch.dto;

public record PlaceDto(
        String name,
        String address,
        String roadAddress,
        int distanceMeters,
        double lat,
        double lng,
        String url
) {
}
