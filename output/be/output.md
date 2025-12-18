#  백엔드 API 요약
- Base URL: `http://localhost:8080`
- 인증: 없음 (내부 사용 가정)
- `kakao.rest-api-key` 미설정 시 장소 검색 응답은 빈 목록을 반환

## 1. 주변 음식점 검색 (`POST /api/places`)
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
- 동작 상세
  - 카카오 로컬 키워드 검색 API `/v2/local/search/keyword.json` 사용
  - 반경 2km, 최대 15건 조회 후 `distanceMeters` 오름차순 정렬
  - 요청/응답 오류가 나도 200 + 빈 배열 반환(서버 로그에만 오류 기록)

## 2. 카카오 지도 링크 반환 (`GET /api/map-link`)
- 설명: 좌표를 받아 카카오 지도 웹 링크와 검색 링크를 반환
- Query Parameters
  - `lat` (number): 위도
  - `lng` (number): 경도
- Response 200 (application/json)
  - `mapUrl` (string): 지정 좌표를 지도로 여는 링크
  - `searchUrl` (string): 동일 명칭(위치)으로 검색하는 링크
- 동작 상세: 고정 명칭 "위치"를 UTF-8로 인코딩해 `map.kakao.com` 링크 구성

## 3. 뷰 렌더링 (`GET /`)
- 설명: `index.html` 템플릿을 렌더링하여 프론트엔드 페이지 제공 (API 아님)

## 설정
- `kakao.rest-api-key`: 백엔드에서 카카오 장소 검색 호출 시 필요 (없으면 `/api/places`는 빈 배열 반환)
- `kakao.javascript-key`: 템플릿에 주입되어 클라이언트 측 카카오 지도 SDK에서 사용
- Swagger UI: 서버 실행 후 `http://localhost:8080/swagger-ui.html`

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
