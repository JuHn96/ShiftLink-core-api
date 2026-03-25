# 🔗 ShiftLink (시링)
> **"더 이상 손으로 계산하지 마세요. 흩어진 모든 소득을 하나로 연결(Link)하는 지능형 N잡러 정산 플랫폼"**
>
> 단순한 달력형 시급 계산기가 아닙니다. 흩어진 알바 일정, 프리랜서 부수입, 복잡한 세금 계산까지. 이제 텍스트 한 줄과 영수증 사진으로 간편하게 기록하세요. AI가 당신의 파편화된 근로 데이터를 똑똑하게 추출하고, 체계적인 정산 시스템이 수당과 공제액을 꼼꼼하게 반영하여 내 손에 들어올 '진짜 수익'을 정확하게 계산해 줍니다.

<br>

## 🛠️ Tech Stack (기술 스택)
| 파트 | 기술 스택 | 비고 (선택 이유) |
| :--- | :--- | :--- |
| **Backend (Core)** | `Kotlin`, `Spring Boot 3.x` | 금융 데이터의 무결성(`BigDecimal`) 및 안정적인 정산 코어 |
| **Backend (AI)** | `Python`, `FastAPI` | 가볍고 빠른 AI 연동(OCR, 자연어 파싱) 마이크로서비스 |
| **Database** | `PostgreSQL`, `MongoDB` | 메인 정형 데이터(RDB)와 비정형 AI 로그(NoSQL)의 완벽한 분리 |
| **Frontend** | `React (TypeScript)`, `Tailwind CSS` | 타입 안정성 및 빠른 반응형 UI 구현 |
| **Infra & DevOps** | `AWS EC2/S3`, `Docker`, `GitHub Actions` | 컨테이너화 및 CI/CD 무중단 배포 파이프라인 |

<br>

## 🚀 Project Progress (진행 상황)
현재 프로젝트의 개발 마일스톤 및 진행 현황입니다. 상세한 개발 로그와 트러블슈팅은 [`docs/playbook.md`](./docs/playbook.md)에서 확인할 수 있습니다.

### Phase 1: 기반 아키텍처 및 환경 세팅
- [x] 프로젝트 기획 및 요구사항 명세서 확정
- [x] AI 에이전트 협업 가이드라인 (`ai_context`) 구축 완비
- [x] 공식 문서화 체계 (`docs`) 디렉토리 세팅
- [ ] Core API (Spring Boot) 프로젝트 초기화 및 GitHub 연동

### Phase 2: 핵심 비즈니스 로직 개발 (Core API)
- [ ] PostgreSQL 연동 및 DB 뼈대(Entity) 설계
- [ ] 사업장(Workplace) 및 근무 기록(WorkLog) 도메인 개발
- [ ] `BigDecimal` 기반 급여/세금 정산 로직 구현

### Phase 3: AI 마이크로서비스 및 프론트 연동
- [ ] FastAPI 서브 서버 구축 및 MongoDB 연동
- [ ] 영수증/텍스트 자연어 파싱 (HITL 적용) 및 UI 통합

---
*💡 프로젝트에 사용된 상세 기술 명세, DB 아키텍처 및 작업 증적 자료는 `docs/` 디렉토리를 참조해 주세요.*