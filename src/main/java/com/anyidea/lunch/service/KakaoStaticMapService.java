package com.anyidea.lunch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class KakaoStaticMapService {

    private static final org.slf4j.Logger log = getLogger(KakaoStaticMapService.class);
    private static final String BASE_URL = "https://dapi.kakao.com";

    private final boolean enabled;
    private final RestClient restClient;

    public KakaoStaticMapService(@Value("${kakao.rest-api-key:}") String restApiKey) {
        this.enabled = StringUtils.hasText(restApiKey);
        this.restClient = enabled
                ? RestClient.builder()
                        .baseUrl(BASE_URL)
                        .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + restApiKey)
                        .build()
                : null;
        if (!enabled) {
            log.warn("kakao.rest-api-key 가 없어 정적 지도 이미지를 생성하지 않습니다.");
        }
    }

    public Optional<byte[]> fetchStaticMap(double lat, double lng, String name) {
        if (!enabled) {
            return Optional.empty();
        }
        try {
            String markerLabel = StringUtils.hasText(name) ? name : "위치";
            String markerParam = "coord:" + lng + "," + lat + ";name:" + urlEncode(markerLabel);
            String centerParam = lng + "," + lat;

            byte[] imageBytes = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/maps/staticmap")
                            .queryParam("center", centerParam)
                            .queryParam("level", 4)
                            .queryParam("w", 640)
                            .queryParam("h", 360)
                            .queryParam("markers", markerParam)
                            .build())
                    .accept(MediaType.IMAGE_PNG)
                    .retrieve()
                    .body(byte[].class);

            return Optional.ofNullable(imageBytes);
        } catch (RestClientResponseException e) {
            log.error("정적 지도 호출 실패 status={} body={}", e.getStatusCode().value(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.error("정적 지도 호출 중 오류", e);
            return Optional.empty();
        }
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }
}
