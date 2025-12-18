package com.anyidea.lunch.controller;

import com.anyidea.lunch.dto.MapLinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@Tag(name = "Map Link", description = "카카오 지도 웹 링크를 반환합니다.")
public class StaticMapController {

    @GetMapping("/api/map-link")
    @Operation(summary = "카카오 지도 링크 반환", description = "정적 이미지 대신 카카오 지도 웹 링크를 반환합니다.")
    public MapLinkResponse getMapLink(
            @RequestParam double lat,
            @RequestParam double lng) {
        String encodedName = urlEncode("위치");
        String mapUrl = "https://map.kakao.com/link/map/" + encodedName + "," + lat + "," + lng;
        String searchUrl = "https://map.kakao.com/link/search/" + encodedName;
        return new MapLinkResponse(mapUrl, searchUrl);
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }
}
