package com.anyidea.lunch.controller;

import com.anyidea.lunch.dto.PlaceSearchRequest;
import com.anyidea.lunch.dto.PlaceSearchResponse;
import com.anyidea.lunch.service.KakaoPlaceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final KakaoPlaceService kakaoPlaceService;

    public PlaceController(KakaoPlaceService kakaoPlaceService) {
        this.kakaoPlaceService = kakaoPlaceService;
    }

    @PostMapping
    public PlaceSearchResponse searchPlaces(@RequestBody PlaceSearchRequest request) {
        return kakaoPlaceService.searchPlaces(request);
    }
}
