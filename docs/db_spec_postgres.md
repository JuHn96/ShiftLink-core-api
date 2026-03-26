# 🗄️ ShiftLink PostgreSQL Database Specification

> **[설계 및 명명 절대 규칙]**
> * **명명 규칙:** 모든 DB, 테이블, 컬럼명은 `snake_case`를 엄수한다.
> * **PK 타입:** 모든 테이블의 기본 키(PK)는 `BIGINT` (Java `Long`)를 사용한다.
> * **금액 타입:** 정산 오차 0%를 위해 돈과 관련된 컬럼은 `DECIMAL(10,2)`를 강제한다.
> * **데이터 보존:** 데이터 삭제 시 실제 삭제가 아닌 `is_deleted = true` (Soft Delete)로 처리한다.
> * **아키텍처:** 단일 DB 내에서 논리적 스키마(`core`, `billing`, `gamification`)를 분리하여 운영한다.

---

## 🏛️ 1. Schema: `core` (핵심 비즈니스, 정산 및 통합 운영)

### ⚙️ 1.1 시스템 및 인프라 제어 (System Control)
#### `system_constants` (법정 시급/세율 등 동적 상수)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 상수 고유 식별자 |
| `key_name` | VARCHAR(50) | UNIQUE, NOT NULL | 키 이름 (예: `MINIMUM_WAGE_2026`, `TAX_RATE_FREE`) |
| `value` | VARCHAR(255) | NOT NULL | 값 (예: `10030`, `3.3`) |
| `description`| VARCHAR(255) | NULLABLE | 설명 ("2026년도 최저임금") |
| `updated_at` | TIMESTAMP | NOT NULL | 변경일 (서버 재배포 없이 실시간 적용용) |

#### `app_versions` (앱 배포 및 강제 업데이트 통제)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 버전 식별자 |
| `os_type` | VARCHAR(20) | NOT NULL | OS 구분 (`AOS`, `IOS`) |
| `latest_version`| VARCHAR(20) | NOT NULL | 최신 배포 버전 (예: `1.2.0`) |
| `min_required_version`| VARCHAR(20)| NOT NULL | 최소 요구 버전 (이 버전 미만은 강제 업데이트 팝업) |
| `created_at` | TIMESTAMP | NOT NULL | 배포일 |

### 📁 1.2 `files` (통합 파일/이미지 마스터)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 파일 고유 식별자 |
| `user_id` | BIGINT | FK, NULLABLE | 업로더 ID (시스템 에셋은 NULL) |
| `s3_url` | VARCHAR(500) | NOT NULL | AWS S3 물리적 이미지 링크 |
| `file_type` | VARCHAR(50) | NOT NULL | MIME Type (`image/png`, `application/pdf` 등) |
| `usage_context`| VARCHAR(50) | NOT NULL | 사용처 (`AVATAR`, `RECEIPT`, `NOTICE_BANNER`, `INQUIRY`) |
| `is_deleted` | BOOLEAN | DEFAULT FALSE| S3 물리적 삭제 대기 여부 (배치 스케줄러용) |
| `created_at` | TIMESTAMP | NOT NULL | 업로드 시간 |

### 👤 1.3 유저 및 권한, 알림 마스터 (Users & Devices)
#### `users` (핵심 유저 원장)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 유저 고유 식별자 |
| `phone_number` | VARCHAR(20) | UNIQUE, NOT NULL | 휴대폰 번호 (OTP 로그인 핵심 수단) |
| `password_hash`| VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| `name` | VARCHAR(50) | NOT NULL | 실명 |
| `nickname` | VARCHAR(50) | NOT NULL | 앱 내 표시 닉네임 |
| `role` | VARCHAR(20) | NOT NULL | 권한 (`ROLE_USER`, `ROLE_ADMIN`) |
| `status` | VARCHAR(20) | NOT NULL | 상태 (`ACTIVE`, `SUSPENDED`, `WITHDRAWN`) |
| **`profile_image_id`**| BIGINT | FK, NULLABLE | 프로필 사진 (`files` 테이블 참조) |
| `is_deleted` | BOOLEAN | DEFAULT FALSE| 탈퇴 여부 |
| `created_at` | TIMESTAMP | NOT NULL | 가입일 |

#### `user_devices` (푸시 알림용 기기 토큰)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 기기 식별자 |
| `user_id` | BIGINT | FK, NOT NULL | 유저 ID |
| `device_token` | VARCHAR(255) | UNIQUE, NOT NULL| FCM 푸시 알림 토큰 (무료 알림용) |
| `os_type` | VARCHAR(20) | NOT NULL | `AOS` 또는 `IOS` |
| `last_login_at`| TIMESTAMP | NOT NULL | 마지막 로그인 시간 (휴면 유저 마케팅용) |

### 🛠️ 1.4 어드민 운영 및 CS (Admin & Customer Service)
#### `inquiries` (1:1 고객센터 및 환불/버그 접수)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 문의 식별자 |
| `user_id` | BIGINT | FK, NOT NULL | 문의한 유저 ID |
| `category` | VARCHAR(50) | NOT NULL | 분류 (`BILLING`(결제/환불), `BUG`(버그), `ACCOUNT`) |
| `title` | VARCHAR(255) | NOT NULL | 문의 제목 |
| `content` | TEXT | NOT NULL | 문의 본문 |
| **`evidence_image_id`**| BIGINT | FK, NULLABLE | 증빙 캡처 이미지 (`files` 참조) |
| `status` | VARCHAR(20) | NOT NULL | 처리 상태 (`WAITING`(대기), `ANSWERED`(답변완료)) |
| `answer_content`| TEXT | NULLABLE | 관리자 답변 내용 |
| `created_at` | TIMESTAMP | NOT NULL | 문의 접수일 |
| `answered_at` | TIMESTAMP | NULLABLE | 답변 완료일 |

*(참고: `admin_content_logs`(공지/이벤트 CMS), `user_sanctions`(블랙리스트), `user_agreements`(동의 관리), `workplaces`(사업장), `work_logs`(근무 기록), `monthly_settlements`(미수금 정산) 테이블도 완벽한 규격으로 core 스키마에 포함됨)*

---

## 💳 2. Schema: `billing` (수익화 및 결제 시스템)

### 2.1 `point_wallets` (통합 포인트 장부)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 지갑 고유 식별자 |
| `user_id` | BIGINT | FK, NOT NULL | 유저 ID |
| `point_type` | VARCHAR(20) | NOT NULL | 종류 (`FREE`: 이벤트 무상, `PAID`: 결제분) |
| `original_amount`| INT | NOT NULL | 최초 지급 양 |
| `remaining_amount`| INT | NOT NULL | 남은 잔액 |
| `expires_at` | TIMESTAMP | NULLABLE | 무료 포인트 만료 일시 (유료는 NULL) |

*(참고: `point_transactions`(증감 이력), `daily_ai_usages`(일일 무료 횟수 추적) 포함)*

---

## 🎮 3. Schema: `gamification` (유저 락인 및 상점)

### 3.1 `items` (아바타 상점 아이템 마스터)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 아이템 식별자 |
| `name` | VARCHAR(100) | NOT NULL | 아이템 이름 |
| `category` | VARCHAR(50) | NOT NULL | 장착 부위 (`HEAD`, `BODY`, `BACKGROUND`) |
| `price` | INT | NOT NULL | 구매 포인트 수량 |
| **`asset_image_id`**| BIGINT | FK, NOT NULL | 아바타 에셋 이미지 (`core.files` 참조) |
| `is_deleted` | BOOLEAN | DEFAULT FALSE| 단종 여부 |

*(참고: `user_inventories`(소유권), `user_equipped_avatars`(현재 장착 상태) 포함)*