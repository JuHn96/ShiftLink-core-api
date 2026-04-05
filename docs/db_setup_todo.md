# Phase 3: 도커 데이터베이스 인프라 기동 및 JPA Entity 설계 ToDo 리스트

이 문서는 개발자가 DBeaver 세팅을 마친 후, 에이전트와 협업하여 Spring Data JPA 기반의 'Code-First(엔티티 ➡️ 테이블 자동 생성)' 핵심 실무를 달성하기 위한 구체적인 작업 로드맵이다.

---

## 🏗️ [Step 1] 인프라 세팅 (빈 DB 컨테이너 기동)
- [x] `docker-compose.yml` 뼈대 파일 생성 (PostgreSQL 15 + 외부 포트 5432 및 `init-db.sql` 마운트 매핑 완료)
- [x] 터미널에서 `docker-compose up -d` 명령어 실행하여 백그라운드에 DB 구동 및 자동 초기화
- [x] DBeaver 프로그램에서 해당 로컬 DB(localhost:5432)에 새롭게 접속하여 연결(테이블 세팅) 여부 확인 

## 🔓 [Step 2] 보안 연결망 구축 (개발 모드 전환)
- [x] `application.yml` 파일의 `ddl-auto: validate` 설정 유지 (AI 오염 방지를 위해 자동 생성을 막고 검증만 수행)
- [x] `.env` 파일 내에 진짜 DB 정보 주입 (특히 IPv6 이슈를 피해 `DB_HOST=127.0.0.1`로 고정, `DB_USERNAME=shiftlink` 동기화)

## 🧬 [Step 3] 엔티티(Entity) 코드 도출 (하이브리드 분업)
- [x] 웹 제미나이(Web AI)에게 `docs/db_spec_postgres.md` 파일 전문을 넘겨주어 Kotlin `@Entity` 클래스 생성 위임
- [x] 매핑 규칙 강제: `BigDecimal` 타입 고정, Enum-To-DB String 매핑 컨버터(`@AttributeConverter`) 탑재
- [x] 생성된 Entity 16개 코드를 로컬 환경으로 복사하여 `core-api/domain/` 하위의 12개 도메인 폴더에 분산 영구 체결

## ✨ [Step 4] 정산 서버 빌드 및 자동 검증 (Magic Moment)
- [x] 터미널에서 로컬 Java 환경(OpenJDK 17) 구축 및 `./gradlew bootRun` 서버 기동
- [x] 터미널 부팅 로그에서 `Hibernate: validate...` 를 거치며 단 하나의 스키마 에러도 없이 100% 무결점 통과
- [x] 크롬 브라우저에서 `localhost:8080` 접속하여 Spring Boot 기동 성공 증명 (Whitelabel 에러 확인)

## 🔒 [Step 5] 작업 종료 및 로깅
- [x] 에러 복구 로그(IPv6 이슈, `application.yml` 임포트 문제 등) 확보
- [x] 이 위대한 Entity-DB 무결성 매핑 과정을 `docs/playbook.md` 및 각종 문서에 성공 일기장으로 증적 갱신. (작업 완전 종료)
