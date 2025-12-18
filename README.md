# 오늘 점심 뭐 먹지?

카카오 장소/지도 API를 이용해 점심 후보를 찾아주는 서비스입니다.

## 실행 방법

### 스프링 부트 (Gradle)
- `./gradlew bootRun`
- `./gradlew clean --refresh-dependencies bootRun` (캐시 초기화 후 실행)

### 도커
- `docker compose build --no-cache && docker compose up` (전경 실행, 로그 확인 용이)
- `docker compose build --no-cache && docker compose up -d` (백그라운드 실행)

## Swagger API 문서
- 서버 실행 후 브라우저에서 `http://localhost:8080/swagger-ui.html` 접속

## 백엔드 API 요약
- Base URL: `http://localhost:8080`
- 인증: 없음 (내부 사용 가정)
- `kakao.rest-api-key` 미설정 시 장소 검색 응답은 빈 목록을 반환

### 1. 주변 음식점 검색 (`POST /api/places`)
- 설명: 위도/경도와 키워드를 받아 반경 2km 내 음식점을 거리순으로 조회
- Request Body (application/json)
  - `keyword` (string): 검색 키워드 (예: "돈까스")
  - `lat` (number): 위도 (예: 33.485947)
  - `lng` (number): 경도 (예: 126.489446)
- Response 200 (application/json)
  - `places` (array)
    - `name` (string): 장소 이름
    - `address` (string): 지번 주소
    - `roadAddress` (string): 도로명 주소
    - `distanceMeters` (number): 현재 위치로부터 거리(미터)
    - `lat` (number): 장소 위도
    - `lng` (number): 장소 경도
    - `url` (string): 카카오 장소 상세 페이지 URL
- 비고: 외부 API 오류/토큰 미설정 시 `places`는 빈 배열

### 2. 카카오 지도 링크 반환 (`GET /api/map-link`)
- 설명: 좌표를 받아 카카오 지도 웹 링크와 검색 링크를 반환
- Query Parameters
  - `lat` (number): 위도
  - `lng` (number): 경도
- Response 200 (application/json)
  - `mapUrl` (string): 지정 좌표를 지도로 여는 링크
  - `searchUrl` (string): 동일 명칭(위치)으로 검색하는 링크

### 3. 뷰 렌더링 (`GET /`)
- 설명: `index.html` 템플릿을 렌더링하여 프론트엔드 페이지 제공 (API 아님)

## 샘플 요청/응답
```http
POST http://localhost:8080/api/places
Content-Type: application/json

{
  "keyword": "돈까스",
  "lat": 33.485947,
  "lng": 126.489446
}

// Response 200
{
  "places": [
    {
      "name": "홍가돈까스",
      "address": "제주특별자치도 제주시 ...",
      "roadAddress": "제주특별자치도 제주시 ...",
      "distanceMeters": 120,
      "lat": 33.4861,
      "lng": 126.4895,
      "url": "https://place.map.kakao.com/123456789"
    }
  ]
}
```
