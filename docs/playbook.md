# 🚀 ShiftLink 프로젝트 플레이북

본 문서는 프로젝트의 성공적인 작업 내역, 변경 이력, 트러블슈팅 요약을 시간순으로 기록하는 메인 작업 로그이다.

---

## 📅 1. 작업 및 성공 로그 (Success Records)
> 오류 없이 완벽하게 구동이 확인된 작업들의 순차적 기록

### 📌 [Phase 1] 기반 아키텍처 및 코어 정산 (Core API)
* **[2026-03-25] ShiftLink 프로젝트 문서화 및 AI 환경 세팅 완료**
  * `ai_context` 디렉토리 기반 글로벌/프로젝트 AI 통제 규칙 수립 완료.
  * `docs` 디렉토리 기반 산출물 및 증적 관리 체계 구축 완료.

* **[2026-03-26] Core API (Spring Boot) 프로젝트 뼈대 생성 성공**
  * 터미널 환경에서 `curl` (Spring Initializr) 명령어로 `core-api/` 프로젝트(Kotlin + Gradle) 다운로드 및 압축 해제 완료. (적용 디펜던시: `web`, `data-jpa`, `postgresql`)
  * **[터미널 해제 성공 로그 증적]**
    ```text
    Archive:  core-api.zip
       creating: core-api/
      inflating: core-api/gradlew        
      inflating: core-api/settings.gradle  
       creating: core-api/src/main/kotlin/com/shiftlink/core_api/
      inflating: core-api/src/main/kotlin/com/shiftlink/core_api/CoreApiApplication.kt  
      inflating: core-api/build.gradle
      ... (정상 해제 완료 성공)
    ```

* **[2026-03-26] Core API 도메인 중심 패키지 아키텍처(DDD) 디렉토리 구축 성공**
  * `mkdir -p` 확장을 통한 다중 트리 명령어 수행으로 `com/shiftlink/core_api/` 내부를 `global`/`domain` 기반 12개 체제로 완벽히 분리. (구조 상세는 `architecture.md` 참조)

* **[2026-03-26] Git 및 DB 환경변수 보안 세팅(Environment Security) 선행 완료**
  * `.properties`를 실무 표준인 `application.yml`로 교체.
  * DB 접속 정보 하드코딩 방지를 위해 `.env` 파일(비밀번호 보관 보안 금고) 최초 세팅 및 `application.yml` 내 `${DB_PASSWORD}` 변수 바인딩 적용.
  * 프로젝트 루트 `.gitignore`에 `.env` 및 `*-secret.yml` 추적 예외 룰을 추가하여 깃허브 보안 유출 사전 원천 차단.

* **[2026-03-27] Gitignore 와일드카드 오용 버그 교정 및 템플릿 예외 처리 완료**
  * `.gitignore`의 `.env.*` 과도한 오버블로킹 때문에 필수 템플릿 파일인 `.env.example`이 깃허브에 무시(차단) 당할 뻔한 버그를 개발자(Human)가 직관적으로 사전 적발함.
  * 하단에 `!.env.example` 부정 문법을 추가하여, 악성 보안 유출은 막으면서 필수 가이드 파일은 깃허브에 완벽하게 푸시(허용)될 수 있도록 성공적으로 정규화함.

## 🔄 2. 설정 및 명세 변경 이력 (Modification Logs)
> 기존의 기술 명세나 아키텍처, 규칙 등이 수정되었을 때 그 사유를 기록

* (아직 변경 이력 없음)

## ⚠️ 3. 트러블슈팅 요약 (Troubleshooting Summary)
> 발생했던 핵심 이슈와 해결 상태를 간단히 요약 (상세 내용은 troubleshooting.md 참조)

* **[2026-03-26] unzip 패키지 누락으로 인한 압축 해제 에러:** `curl`을 통해 다운로드한 프로젝트 뼈대의 압축을 해제할 때 `unzip` 유틸리티 부재로 실패함. 👉 `sudo apt install unzip`으로 유틸리티 설치 후 정상 압축 해제 성공. (상세 내역 `troubleshooting.md` 참고)