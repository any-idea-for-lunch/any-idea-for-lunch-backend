package com.anyidea.lunch.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceSearchRequest(
        @Schema(description = "검색 키워드", example = "돈까스")
        String keyword,
        @Schema(description = "위도", example = "33.485947")
        double lat,
        @Schema(description = "경도", example = "126.489446")
        double lng
) {
}
