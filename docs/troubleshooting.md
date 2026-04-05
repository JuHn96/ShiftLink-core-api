# ⚠️ ShiftLink 트러블슈팅 상세 기록

본 문서는 프로젝트 진행 중 발생한 심각한 오류나 버그의 원인을 심도 있게 분석하고 해결 과정을 기록하는 문서이다.

---

## 템플릿 양식 (작성 시 복사하여 사용)
### 🐛 [YYYY-MM-DD] (에러 요약 제목)
* **[Issue] 현상 및 에러 로그:**
  * (어떤 상황에서 어떤 에러 메시지가 발생했는지 기록)
* **[Cause] 원인 분석:**
  * (왜 이런 문제가 발생했는지 기술적/아키텍처적 관점에서 분석)
* **[Resolution] 해결 과정 및 결과:**
  * (어떤 시도를 했고, 최종적으로 어떻게 해결했는지 코드/설정 변경 내역 기록)

---

### 🐛 [2026-03-26] 우분투 unzip 패키지 누락으로 인한 압축 해제 실패
* **[Issue] 현상 및 에러 로그:**
  * Spring Boot 뼈대 프로젝트 zip 파일을 다운로드한 직후 압축을 풀려 할 때 아래 에러 발생.
  ```text
  Command 'unzip' not found, but can be installed with:
  sudo apt install unzip
  ```
* **[Cause] 원인 분석:**
  * 우분투 리눅스 환경에 `unzip` 유틸리티 패키지가 설치되지 않아 명령어 실행 실패.
* **[Resolution] 해결 과정 및 결과:**
  * `sudo apt update && sudo apt install -y unzip` 명령어로 누락된 패키지를 직접 설치한 뒤, 파일을 해제(inflating)하는 데 최종 성공함.

---

### 🐛 [2026-04-05] PostgreSQL의 ON UPDATE CURRENT_TIMESTAMP 미지원 문법 이슈
* **[Issue] 현상 및 에러 로그:**
  * 테이블 설계 시 로우 데이터가 수정될 때마다 `updated_at` 컬럼 시간을 자동 갱신하려 했으나, MySQL에서 흔히 쓰이는 `ON UPDATE CURRENT_TIMESTAMP` 문법이 적용되지 않음 (PostgreSQL 미지원).
* **[Cause] 원인 분석:**
  * PostgreSQL은 SQL 표준을 매우 엄격하게 따르기 때문에, 특정 DBMS 종속 편의성 구문(단축어)을 제공하지 않고 명시적인 함수(Function) 사용을 강제하는 철학을 가지고 있음.
* **[Resolution] 해결 과정 및 결과:**
  * 시스템 전역(Global)에서 `NEW.updated_at = NOW();` 처리를 수행하는 공통 `update_timestamp()` 트리거 함수를 수동으로 새롭게 작성함.
  * 업데이트 시간이 필요한 모든 테이블(`users`, `workplaces` 등)에 `CREATE TRIGGER` 문을 삽입하여, 애플리케이션 외부(DB)에서도 스스로 안전하게 수정 시간을 갱신하도록 완벽 복구 완료.

---

### 🐛 [2026-04-05] AI 에이전트의 컨텍스트 환각(Hallucination) 및 맹점 통제
* **[Issue] 현상 및 에러 로그:**
  * 새 아키텍처 규칙을 문서화하라는 지시를 받은 AI 에이전트가 기존 정상 디렉토리인 `ai_context` 폴더의 존재를 파악하지 않고 `@ai_context`라는 중복 복제 폴더 구조를 멋대로 생성함.
* **[Cause] 원인 분석:**
  * AI가 과거 대화에서 언급된 특정 문자열(`@ai_context`)의 캐시 기억에만 과의존하여, 실행 전 `list_dir` 등으로 실제 디렉토리 구조 스캔 절차를 누락함.
* **[Resolution] 해결 과정 및 결과:**
  * 개발자(Human)가 AI의 맹점을 즉각 매처럼 잡아내어 중복 폴더를 터미널 커맨드(`rm -r`)로 날리도록 통제하고, 올바른 원본 파일(`ai_context/guideline.md`) 위치로 궤도를 수정시켜 해당 규약을 병합(Merge)시키는 완벽한 위기관리 능력을 발휘함.

---

### 🐛 [2026-04-06] Java Spring Boot의 IPv6 localhost 바인딩 거부 (DB 연결 실패) 
* **[Issue] 현상 및 에러 로그:**
  * Docker Compose로 띄운 DB를 향해 Kotlin 코드를 구동(`bootRun`) 시 `Unable to determine Dialect without JDBC metadata` 에러와 함께 DB 연결이 거부됨.
* **[Cause] 원인 분석:**
  * 로컬 리눅스의 `localhost` 호스트명이 Java/Spring에서는 IPv6 루프백 주소(`::1`)로 우선 평가되어, 도커가 열어둔 IPv4(`127.0.0.1:5432`)를 찾아가지 못하고 통신 포트 미스매치가 발생함.
* **[Resolution] 해결 과정 및 결과:**
  * `.env` 환경 변수 파일 내 `DB_HOST=localhost`를 `DB_HOST=127.0.0.1`로 명시적으로 교체하여 강제 IPv4 바인딩을 지시함으로써 무결점 Entity 자동 매핑을 즉각 관철함.