# 🏛️ ShiftLink 아키텍처 및 디렉토리 명세

본 문서는 프로젝트의 백엔드, 프론트엔드, AI 각각의 기본 아키텍처 구조와 역할 도메인을 명세한다.

## 1. Core API (Spring Boot) 패키지 구조
현재 핵심 백엔드(Core)는 유지보수성과 확장성을 저해하는 단순 계층형(Layered) 구조를 지양하고, 철저한 **도메인(역할) 중심 패키지 구조(Package by Feature)**를 강제한다.

```text
com.shiftlink.core_api
├── ⚙️ global/              # 프로젝트 전역 공용 프레임워크 뼈대
│   ├── config/             # (JPA, Security, WebMvc 등 환경 설정)
│   ├── exception/          # (공통 에러 핸들러 및 사용자 정의 Exception)
│   ├── security/           # (JWT 인증 인가, 전역 보안 필터 로직)
│   └── common/             # (공통 API 응답 규격 DTO, Utils)
│
└── 💼 domain/              # 비즈니스별로 철저히 독립/격리된 핵심 공간
    ├── user/               # 유저 가입/탈퇴, 자체 권한 관리 로직
    ├── workplace/          # 사업장 기초(기본 시급/적용수당 여부) 정보 세팅
    ├── worklog/            # 출퇴근 시간 로깅 및 근무 상태(지각/조퇴) 기록
    ├── settlement/         # [오차 0%] 급여/휴게시간/주휴수당 자동 정산 엔진 Core
    ├── file/               # 시스템 에셋 메타데이터 및 AWS S3 이미지 연동 모듈
    ├── billing/            # 유·무료 포인트 지갑 상태 및 결제 장부 트랜잭션 추적
    ├── gamification/       # 아바타 상점, 아이템 구매 및 인벤토리 장착 시스템
    └── cs/                 # 1:1 오류 제보 및 고객센터 문의 로직
```
