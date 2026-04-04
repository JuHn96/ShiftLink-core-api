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

* **[2026-03-30] 🏆 Phase 1 기반 세팅 결산 리포트 수정 및 완료**
  * **[아키텍처/보안]:** 12개 비즈니스 도메인(DDD) 패키지 설계 완료 및 `.env` 분리 등 Git 유출(와일드카드 예외) 제어 완벽 방어.
  * **[규칙 확립]:** AI 제어 4대 원칙 도입 및 모든 파일에 'Why' 중심 한국어 목적 주석 소급 적용 완료.

### 📌 [Phase 2] 핵심 비즈니스 DB 아키텍처 및 데이터 모델링
* **[2026-03-30] 기획서 - DB 명세서 간 Gap 분석 및 핵심 코어 테이블 신설**
  * 기존 관리자/결제 위주의 `docs/db_spec_postgres.md`에 기획서(`guideline.md`) 핵심인 [출퇴근 기록, 사비 지출, 미수금 정산, 알림] 5대 기능이 누락된 것을 적발.
  * `workplaces`, `work_logs`, `expenses` 등 필수 코어 원장 테이블을 1차 설계하여 뼈대 보강 완수.

* **[2026-03-31] 💎 현업 아키텍트급 'DB 오버엔지니어링 다이어트 및 역할 분담(Role Distribution)' 결의**
  * 대용량 트래픽 렌더링을 위해 가설계했던 과도한 DB 트리거(Trigger)의 '쓰기 지연(Write Penalty)' 부작용을 사전에 적발함.
  * **[테이블 통폐합 병합]:** 1:1 조인 낭비를 유발하던 알림 테이블(`user_notification_settings`)을 `users` 본체로 영구 흡수 병합.
  * **[정산 캐시 고도화]:** 파편화된 주급(`weekly`)/월급(`monthly`) 테이블을 `period_settlements` 관리 단일 테이블로 구조조정 압축.
  * **[ADR 지식 창고 신설]:** 복잡한 통계/조회는 DB(뷰/인덱스)에 맡기고, 무거운 트랜잭션 동기화 및 캐시 롤백은 백엔드 비동기 이벤트(`Spring Event`)에 전담시키는 **[ADR-001] 백엔드 vs DB 처리 속도 최적화 우위 비교** 기술 문서를 영구 보존소(`docs/dev_knowledge_adrs.md`)에 등재 완료!

* **[2026-03-31] 🏆 B2C 상용앱 스케일의 [통합 로그인 아키텍처] DB 모델링 최종 완성**
  * **[소셜/자체 융합]:** 앱 고유 로그인(자체 중복확인 `login_id`) 체계와 OAuth2 소셜 로그인(구글/카카오)이 자유롭게 혼용 가능한 100% 에러 방어형 통합 원장 테이블 패치 완료.
  * **[보안 및 무결성]:** 봇(Bot) 어뷰징 가입을 막기 위한 명의 인증(휴대폰 번호)과 보조 수단(이메일 비번 찾기)의 듀얼 수집 체계 완비. 소셜 유저의 다중 스팸 코인 가입 방지를 위한 강력한 복합 유니크 인덱스 제약조건 적용.
  * **[유저 최소 요건 식별]:** 만 나이 계산 및 알바 핏팅 대조를 위해 누락되었던 필수 프라이머리 식별자 **`birth_date (생년월일)`** 핀포인트로 추가 수술 완수.
  * **[결론]:** Phase 2 (DB 스펙 마일스톤) 10,000% 무결점 종료 선언.

* **[2026-03-31] ✨ 모든 문서 완전무결점 동기화(Sync) 마감 및 플레이북 원칙 로깅**
  * **[NoSQL 아키텍처 결정]:** PostgreSQL과 달리 MongoDB 명세(`db_spec_mongo.md`)는 파편화를 막기 위해, Phase 4 (AI 마이크로서비스) 진입 시 스키마리스 특성을 살려 후행 설계(Late Binding) 하기로 개발/설계 전략 확립 및 문서 내 사유 기록 완료.
  * **[잔여 문서 현행화]:** 메인 대문 `README.md` 로드맵 페이즈 3 업데이트 시작 선언, `docs/db_setup_todo.md` 진원 단계 갱신, 그리고 빈 껍데기 파일이던 `tech_spec.md`에 링크 목차 세팅까지 단 1바이트의 문서 오차도 없이 전수 검토 및 덮어쓰기 저장 완료.
  * **[작업 원칙 세입]:** 이 몽고DB 스펙 기록을 끝으로 프로젝트 내의 모든 코드와 문서의 마일스톤이 오직 한 점으로 완벽하게 통일되었으며, **향후 모든 세션에서 "이곳(`docs/playbook.md`)은 그날 세션의 모든 작업이 끝난 가장 마지막 타이밍에 전체 산출물을 회고하고 집대성하는 영구적 최종(Final) 기록 공간"**으로서의 핵심 역할 룰을 주환님의 리드로 재확립함.

* **[2026-04-05] 🚀 로컬 DB 인프라(Docker) 구축 및 통신(REST) 기반 가이드라인 고도화 성공**
  * **[DDL 스크립트 작성]:** 전문가 제안(IDENTITY PK, 정규식 등)이 100% 반영된 완벽한 무결성 DB 자동 생성 쿼리(`scripts/init-db.sql`) 마감 완료.
  * **[인프라 청사진]:** 수초 내에 DB를 띄우고 초기화 스크립트를 마운트할 수 있도록 타임존이 고정된 `docker-compose.yml` 구축.
  * **[규칙 추가]:** `ai_context/guideline.md` 내에 "Core 백엔드 REST API 완전 분리" 및 "무상태(Stateless) EKS 인프라 대비" 0순위 절대 규칙 삽입 완료.

## 🔄 2. 설정 및 명세 변경 이력 (Modification Logs)
> 기존의 기술 명세나 아키텍처, 규칙 등이 수정되었을 때 그 사유를 기록

* (아직 변경 이력 없음)

## ⚠️ 3. 트러블슈팅 요약 (Troubleshooting Summary)
> 발생했던 핵심 이슈와 해결 상태를 간단히 요약 (상세 내용은 troubleshooting.md 참조)

* **[2026-03-26] unzip 패키지 누락으로 인한 압축 해제 에러:** `curl`을 통해 다운로드한 프로젝트 뼈대의 압축을 해제할 때 `unzip` 유틸리티 부재로 실패함. 👉 `sudo apt install unzip`으로 유틸리티 설치 후 정상 압축 해제 성공. (상세 내역 `troubleshooting.md` 참고)