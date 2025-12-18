package com.anyidea.lunch.service;

import com.anyidea.lunch.dto.PlaceDto;
import com.anyidea.lunch.dto.PlaceSearchRequest;
import com.anyidea.lunch.dto.PlaceSearchResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Comparator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class KakaoPlaceService {

    private static final String BASE_URL = "https://dapi.kakao.com";

    private static final org.slf4j.Logger log = getLogger(KakaoPlaceService.class);

    private final RestClient restClient;
    private final boolean enabled;

    public KakaoPlaceService(@Value("${kakao.rest-api-key:}") String restApiKey) {
        this.enabled = StringUtils.hasText(restApiKey);
        this.restClient = enabled
                ? RestClient.builder()
                        .baseUrl(BASE_URL)
                        .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + restApiKey)
                        .build()
                : null;
        if (!enabled) {
            log.warn("kakao.rest-api-key 가 설정되지 않아 장소 검색을 건너뜁니다.");
        }
    }

    public PlaceSearchResponse searchPlaces(PlaceSearchRequest request) {
        if (!enabled) {
            return new PlaceSearchResponse(List.of());
        }

        try {
            KakaoSearchResponse kakaoResponse = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", request.keyword())
                            .queryParam("x", request.lng())
                            .queryParam("y", request.lat())
                            .queryParam("radius", 2000)
                            .queryParam("size", 15)
                            .build())
                    .retrieve()
                    .body(KakaoSearchResponse.class);

            List<PlaceDto> places = kakaoResponse == null ? List.of()
                    : kakaoResponse.documents().stream()
                            .map(doc -> new PlaceDto(
                                    doc.placeName,
                                    doc.addressName,
                                    doc.roadAddressName,
                                    parseDistance(doc.distance),
                                    doc.y,
                                    doc.x,
                                    doc.placeUrl))
                            .sorted(Comparator.comparingInt(PlaceDto::distanceMeters))
                            .toList();

            return new PlaceSearchResponse(places);
        } catch (RestClientResponseException e) {
            HttpStatusCode status = e.getStatusCode();
            log.error("카카오 장소 검색 실패 status={} body={}", status.value(), e.getResponseBodyAsString());
            return new PlaceSearchResponse(List.of());
        } catch (Exception e) {
            log.error("카카오 장소 검색 중 알 수 없는 오류", e);
            return new PlaceSearchResponse(List.of());
        }
    }

    private int parseDistance(String distance) {
        try {
            return Integer.parseInt(distance);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private record KakaoSearchResponse(
            List<KakaoPlaceDocument> documents) {
    }

    private record KakaoPlaceDocument(
            @JsonProperty("place_name") String placeName,
            @JsonProperty("address_name") String addressName,
            @JsonProperty("road_address_name") String roadAddressName,
            double x,
            double y,
            String distance,
            @JsonProperty("place_url") String placeUrl) {
    }
}
