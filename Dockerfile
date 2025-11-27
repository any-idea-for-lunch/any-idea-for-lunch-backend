# 1단계: 빌드 (Gradle + JDK 17)
FROM gradle:8.10-jdk17 AS builder
WORKDIR /home/gradle/project

# 소스 전체 복사
COPY . .

# Spring Boot 실행용 JAR 빌드
RUN gradle bootJar --no-daemon

# 2단계: 런타임 (가벼운 JRE 17)
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드된 JAR을 복사 (이름은 *-SNAPSHOT.jar 형태라 와일드카드 사용)
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
