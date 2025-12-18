# 오늘 점심 뭐 먹지?

카카오 장소/지도 API를 이용해 점심 후보를 찾아주는 서비스입니다.

## 실행 환경 정보
- Java Runtime 17 버전 이상 필요

## 실행 방법

### 스프링 부트 (Gradle)
- `./gradlew bootRun`
- `./gradlew clean --refresh-dependencies bootRun` (캐시 초기화 후 실행)

### 도커
- `docker compose build --no-cache && docker compose up` (전경 실행, 로그 확인 용이)
- `docker compose build --no-cache && docker compose up -d` (백그라운드 실행)

## 배포 URL
- Base URL: `http://localhost:8080`
- Swagger API docs: 서버 실행 후 브라우저에서 `http://localhost:8080/swagger-ui.html` 접속

## 전체 구조도
(추가 필요)

## 페이지 스크린샷
(추가 필요)
