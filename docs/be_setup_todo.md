# Phase 4: Backend API & Service Logic ToDo 리스트 (Core API)

이 문서는 Phase 3(JPA 엔티티 매핑)가 성공적으로 완료된 후, 실제 프론트엔드와 소통할 **REST API 계층 및 비즈니스 로직(Service)**을 개발하기 위한 백엔드 전용 작업 로드맵이다.

---

## 🏗️ [Step 1] Spring Boot 전역 환경(Global) 세팅
- [ ] **에러 핸들러 구축:** `@RestControllerAdvice` 기반의 전역 예외 처리(Global Exception Handler) 및 공통 응답 DTO(BaseResponse) 래퍼 설계
- [ ] **Swagger/OpenAPI 연동:** 프론트엔드 개발자가 API 스펙을 바로 확인할 수 있는 대시보드(http://localhost:8080/swagger-ui) 자동화 세팅
- [ ] **CORS 오픈:** 로컬 프론트엔드 환경(Vite 등)의 API 호출을 허용하는 WebMvcConfig 설정

## 🔐 [Step 2] Spring Security & 통합 인증 (Authentication)
- [ ] `SecurityConfig.kt` 초기 세팅 (JWT 토큰 검증 필터, 인증 라우팅 분류)
- [ ] 소셜 로그인(OAuth2) / 자체 가입 통합 인증 로직용 `AuthService` 개발
- [ ] JWT(Access/Refresh) 발급, Redis(또는 DB) 기반 Refresh Token 관리 체계 구축

## 💼 [Step 3] 핵심 도메인 API 개발 (DDD 기반)
- [ ] **유저(User):** 회원가입, 내 정보 조회/수정 API, 프로필 이미지 업로드(S3 연동)
- [ ] **근무지(Workplace):** 근무지 추가/수정, 테마 색상 및 세금 종류 할당 API
- [ ] **근무 기록(WorkLog) & 지출:** 출/퇴근 태깅, 휴게시간 수정, 영수증(사비 지출) 등록 API 
- [ ] **정산(Settlement):** 핵심! `BigDecimal` 기반 주휴수당/세금 오차 0% 계산 및 미수금 렌더링 로직 개발

## ✅ [Step 4] 통합 테스트(Integration Test)
- [ ] JUnit5 + MockMvc를 이용한 핵심 Controller 테스트 케이스(TDD) 작성
- [ ] Testcontainers(테스트 전용 도커 DB) 기반 격리 테스트 구동 확인

## 🚀 [Step 5] API 배포 준비
- [ ] 모든 로직 점검 후 프론트엔드 팀에게 API 명세서(Swagger Link) 인계
- [ ] AWS EC2 무중단 배포를 위한 `Dockerfile` 멀티스테이징 최적화 적용
