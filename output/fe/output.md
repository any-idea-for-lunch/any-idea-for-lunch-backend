기술 스택 (Tech Stack)
Language: Java 17
Framework: Spring Boot 3.5.8
Frontend Engine: HTML/CSS/JS

## 사전 요구 사항
Java JDK 17이상 (필수)

<설치>
이 프로젝트는 별도의 프론트엔드 빌드 도구 없이, Java와 Spring Boot만으로 실행되는 통합형 서비스입니다.

<실행>
터미널(또는 CMD)에서 아래 명령어를 입력하세요.

# 1. 저장소 복제
git clone https://github.com/any-idea-for-lunch/any-idea-for-lunch-backend.git
cd any-idea-for-lunch-backend

# 2. 서버 실행 (Gradle Wrapper 사용)
    # Mac / Linux:
    ./gradlew bootRun

# Windows:
    gradlew.bat bootRun


<구조>
```text
src/main/
├── java/com/anyidea/lunch/
│   ├── controller/          # API 및 페이지 라우팅 컨트롤러
│   ├── service/             # 추천 로직 및 비즈니스 로직
│   └── AnyIdeaForLunchApplication.java  # 메인 실행 클래스
└── resources/
    ├── templates/           # 프론트엔드 HTML
    │   └── index.html       # 메인 화면
    ├── static/              # 정적 자원 (프론트엔드 핵심 파일)
    │   ├── css/             # 스타일시트
    │   └── js/              # API 통신 및 화면 로직 (Vanilla JS)
    └── application.properties # 서버 설정 (포트, DB 등)
```


## API 연동 설명
본 프로젝트는 REST API를 통해 데이터를 주고받으며, 프론트엔드(JavaScript)에서 이를 호출하여 화면을 갱신합니다.

1. API 문서 확인 (Swagger)
서버 실행 후 Swagger UI에 접속하면 사용 가능한 모든 API 엔드포인트와 데이터 형식을 시각적으로 확인할 수 있습니다.

2. 프론트엔드 연동 방식
src/main/resources/static/js/ 내의 스크립트 파일에서 브라우저 내장 fetch API를 사용하여 백엔드 데이터를 가져옵니다.