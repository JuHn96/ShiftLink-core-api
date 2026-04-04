# Phase 3: 도커 데이터베이스 인프라 기동 및 JPA Entity 설계 ToDo 리스트

이 문서는 개발자가 DBeaver 세팅을 마친 후, 에이전트와 협업하여 Spring Data JPA 기반의 'Code-First(엔티티 ➡️ 테이블 자동 생성)' 핵심 실무를 달성하기 위한 구체적인 작업 로드맵이다.

---

## 🏗️ [Step 1] 인프라 세팅 (빈 DB 컨테이너 기동)
- [x] `docker-compose.yml` 뼈대 파일 생성 (PostgreSQL 15 + 외부 포트 5432 및 `init-db.sql` 마운트 매핑 완료)
- [ ] 터미널에서 `docker-compose up -d` 명령어 실행하여 백그라운드에 DB 구동 및 자동 초기화
- [ ] DBeaver 프로그램에서 해당 로컬 DB(localhost:5432)에 새롭게 접속하여 연결(테이블 세팅) 여부 확인 

## 🔓 [Step 2] 보안 자물쇠 해제 (개발 모드 전환)
- [ ] `application.yml` 파일의 `ddl-auto: validate` 설정을 `update`로 임시 변경 (테이블 자동 생성을 허가)
- [ ] `.env` 파일 내에 진짜 DB 비밀번호 입력 (DBeaver에 접속한 비밀번호와 동일하게 맞춤)

## 🧬 [Step 3] 엔티티(Entity) 코드 도출 (하이브리드 분업)
- [ ] 웹 제미나이(Web AI)에게 `docs/db_spec_postgres.md` 파일 전문을 넘겨주어 Kotlin `@Entity` 클래스 코드 전체 무한 자동 생성 지시
- [ ] 생성된 Entity 코드를 로컬 환경으로 복사하여 `core-api/.../domain/` 하위의 각 도메인 전용 폴더(`user`, `worklog` 등)에 깔끔하게 나누어 배치

## ✨ [Step 4] 정산 서버 빌드 및 자동 생성 (Magic Moment)
- [ ] `CoreApiApplication.kt` 메인 앱 실행 (Spring Boot 서버 기동)
- [ ] 터미널 부팅 로그에서 `Hibernate: create table...` 및 `alter table... add constraint foreign key` 등 테이블과 관계(연결)가 연쇄적으로 폭발하며 생성되는 로그 감상
- [ ] DBeaver에서 새로고침(`F5`)하여, 명세서대로 테이블명, 데이터 타입, 그리고 외래키(FK) 연결 거미줄이 100% 동일하게 생성되었는지 최종 시각적 검증!

## 🔒 [Step 5] 보안 롤백 (안전망 복구) 및 로깅
- [ ] 테이블 생성이 완벽히 성공 렌더링된 것이 확인되면, 실수로 DB를 또 날리는 대참사를 막기 위해 즉시 `application.yml`의 `ddl-auto`를 `validate`로 되잠금(Lock).
- [ ] 이 위대한 Code-First 달성 과정을 `docs/playbook.md` 성공 일기장에 증적으로 로깅.
