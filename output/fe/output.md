## Frontend 원본 리포지토리
https://github.com/any-idea-for-lunch/any-idea-for-lunch-frontend

본 리포지토리는 순수 HTML / CSS / JavaScript 기반으로 작성된 프론트엔드 코드입니다. 프레임워크(React, Vue 등)는 사용하지 않았으며, 브라우저 환경에서 바로 동작하는 구조를 목표로 합니다.

## 기술 스택 (Tech Stack)
- HTML5
- CSS3
- JavaScript
- Live Server (로컬 개발용)

FE 개발 단계에서는 Node.js, npm, 프론트엔드 프레임워크를 사용하지 않았습니다. 브라우저에서 바로 실행 가능한 구조로 구현하여, 백엔드와의 통합을 고려한 정적 FE 개발을 진행했습니다.

## 실행 방법 (Frontend 단독 실행)

Frontend 단독 테스트 및 화면 확인은 Live Server를 사용하여 로컬에서 실행합니다.

### <실행 절차>

1. 레포지토리 클론
git clone https://github.com/any-idea-for-lunch/any-idea-for-lunch-frontend

2. 프로젝트 폴더 열기 (VS Code 권장)

3. index.html 파일 우클릭 → Open with Live Server

4. 브라우저에서 자동 실행
이 단계에서는 백엔드 API 없이도 화면 흐름 및 UI 동작을 테스트할 수 있습니다.

### <구조>
```text
frontend/
├── index.html # 메인 화면 (메뉴 추천 UI)
├── script.js # 화면 로직 및 API 통신 (Vanilla JS)
├── aa.css # 기본 스타일
├── mandal.css # 만다라트 UI 관련 스타일
├── 다음 컴포넌트css 
├── README.md # Frontend 설명 문서
└── .gitignore
```
- HTML: 화면 구조 정의

- CSS: 레이아웃 및 디자인 구현

- JS: 사용자 이벤트 처리 및 API 통신

## API 연동 설명
Frontend 리포지토리에서는 다음을 중심으로 작업했습니다.

- 화면 레이아웃 구성

- 사용자 인터랙션 구현

- API 연동을 가정한 목업(mock) 데이터 테스트

이 단계에서는 실제 백엔드 서버와 직접 연결하지 않고, API 응답을 가정한 구조로 JavaScript 코드를 작성했습니다.

### 통합 방식
- Backend 리포지토리의
```text
src/main/resources/templates/
```
(Thymeleaf 템플릿 영역) 안에 FE 코드를 수동으로 이동

정적 리소스는
```text
src/main/resources/static/
```
경로에 배치