package com.anyidea.lunch.controller;

import com.anyidea.lunch.service.KakaoStaticMapService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StaticMapController {

    private final KakaoStaticMapService staticMapService;

    public StaticMapController(KakaoStaticMapService staticMapService) {
        this.staticMapService = staticMapService;
    }

    @GetMapping(value = "/api/static-map", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getStaticMap(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false, defaultValue = "") String name) {

        Optional<byte[]> image = staticMapService.fetchStaticMap(lat, lng, name);

        return image
                .map(bytes -> ResponseEntity
                        .ok()
                        .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                        .body(bytes))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }
}
