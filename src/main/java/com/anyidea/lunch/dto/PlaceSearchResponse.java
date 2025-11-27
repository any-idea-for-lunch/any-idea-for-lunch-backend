package com.anyidea.lunch.dto;

import java.util.List;

public record PlaceSearchResponse(
        List<PlaceDto> places
) {
}
