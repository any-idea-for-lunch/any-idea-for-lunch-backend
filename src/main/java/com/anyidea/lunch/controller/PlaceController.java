package com.anyidea.lunch.controller;

import com.anyidea.lunch.dto.PlaceSearchRequest;
import com.anyidea.lunch.dto.PlaceSearchResponse;
import com.anyidea.lunch.service.KakaoPlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Place Search", description = "카카오 장소 검색 API를 이용해 주변 음식점을 조회합니다.")
@RequestMapping("/api/places")
public class PlaceController {

    private final KakaoPlaceService kakaoPlaceService;

    public PlaceController(KakaoPlaceService kakaoPlaceService) {
        this.kakaoPlaceService = kakaoPlaceService;
    }

    @PostMapping
    @Operation(
            summary = "주변 음식점 검색",
            description = "사용자 위치(lat, lng)와 키워드로 반경 2km 내 장소를 검색합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "검색 키워드와 위경도",
                    content = @Content(
                            schema = @Schema(implementation = PlaceSearchRequest.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                                    {
                                      "keyword": "돈까스",
                                      "lat": 33.485947,
                                      "lng": 126.489446
                                    }
                                    """
                            )
                    )
            )
    )
    public PlaceSearchResponse searchPlaces(@RequestBody PlaceSearchRequest request) {
        return kakaoPlaceService.searchPlaces(request);
    }
}
