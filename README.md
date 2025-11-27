# any-idea-for-lunch-backend

스프링부트 빌드&런
./gradlew bootRun

도커 빌드&런
터미널 빌드&런(로그 쉽게 확인가능)
docker compose build --no-cache && docker compose up

터미널 백그라운드 빌드&런(로그 확인시 터미널에서 도커 컨테이너 내부로 접속해야함)
docker compose build --no-cache && docker compose up -d
