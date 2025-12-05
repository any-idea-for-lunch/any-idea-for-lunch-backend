# 오늘 점심 뭐먹지?

-- 스프링부트 빌드&런
<br />
./gradlew bootRun
<br />
./gradlew clean --refresh-dependencies bootRun
<br />
<br />
-- 도커 빌드&런
<br />

-   터미널 빌드&런(로그 쉽게 확인가능)
    <br />
    docker compose build --no-cache && docker compose up
    <br />
    <br />
-   터미널 백그라운드 빌드&런(로그 확인시 터미널에서 도커 컨테이너 내부로 접속해야함)
    <br />
    docker compose build --no-cache && docker compose up -d
