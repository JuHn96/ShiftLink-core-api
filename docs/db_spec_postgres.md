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
#### `users` (통합 유저 원장: 자체 로그인 + 소셜 기획 융합)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 유저 고유 식별자 |
| `login_id` | VARCHAR(50) | UNIQUE, NULLABLE | 자체 회원가입 시 중복 확인을 거치는 접속 아이디 (소셜 가입자는 NULL) |
| `auth_provider`| VARCHAR(20) | NOT NULL | 가입 채널 (`LOCAL`(앱), `KAKAO`, `NAVER`, `GOOGLE`) |
| `provider_id` | VARCHAR(100)| NULLABLE | 소셜 로그인 시 외부 서버에서 쏴주는 유저 고유 해시 키 |
| `password_hash`| VARCHAR(255) | NULLABLE | 소셜 가입자는 암호를 안치므로 백엔드 에러 방지용으로 NULL 허용(유연성) |
| `phone_number` | VARCHAR(20) | UNIQUE, NULLABLE | 📱 무분별한 봇(Bot) 가입 방지용 명의 인증 (소셜로 연동 직후 추가 기입받음) |
| `email` | VARCHAR(100) | NULLABLE | ✉️ 폰번호 바뀐 유저의 비번찾기 및 마케팅용 (카카오 등 소셜에서 이메일 미제공 구멍 방지용 NULL 허용) |

*(강력 최적화: `auth_provider`와 `provider_id` 두 개를 묶은 복합 유니크 인덱스를 추가하여, 동일한 카카오 계정으로 두 번 가입 시 백엔드 단에서 충돌하기 전에 DB 단에서 무결성 에러를 즉시 차단합니다)*
| `name` | VARCHAR(50) | NOT NULL | 실명 |
| `birth_date` | DATE | NULLABLE | 🎂 생년월일 (본인 인증 및 만 15세 알바생 등 연령 확인용) |
| `nickname` | VARCHAR(50) | NOT NULL | 앱 내 표시 닉네임 |
| `role` | VARCHAR(20) | NOT NULL | 권한 (`ROLE_USER`, `ROLE_ADMIN`) |
| `status` | VARCHAR(20) | NOT NULL | 상태 (`ACTIVE`, `SUSPENDED`, `WITHDRAWN`) |
| **`profile_image_id`**| BIGINT | FK, NULLABLE | 프로필 사진 (`files` 테이블 참조) |
| `is_payday_warning_on` | BOOLEAN | DEFAULT TRUE | [흡수 병합] 정산일 미수금 경고 수신 여부 |
| `is_clockout_reminder_on` | BOOLEAN | DEFAULT TRUE | [흡수 병합] 퇴근 시간 리마인드 수신 여부 |
| `is_deleted` | BOOLEAN | DEFAULT FALSE| (Soft Delete) 앱 탈퇴 여부 |
| `created_at` | TIMESTAMP | NOT NULL | 서비스 가입일 |
| `updated_at` | TIMESTAMP | NOT NULL | 회원정보/설정 수정일 |

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

### 🏢 1.5 핵심 비즈니스: 사업장 및 정산 룰 (Workplaces)
#### `workplaces` (사업장 프로필)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 사업장 고유 식별자 |
| `user_id` | BIGINT | FK, NOT NULL | 소유 유저 ID (인덱스 필요) |
| `name` | VARCHAR(100) | NOT NULL | 업체명 (예: '스타벅스 강남점') |
| `theme_color` | VARCHAR(10) | NOT NULL | 캘린더용 테마 색상 헥스코드 (예: `#00704A`) |
| `base_hourly_wage`| DECIMAL(10,2)| NOT NULL | 기본 시급 (정산 오차 0%를 위해 데시멀) |
| `payday` | INT | NOT NULL | 월급 지급일 (1~31) |
| `tax_rate_type` | VARCHAR(20) | NOT NULL | 세금 공제율 형태 (`3.3`, `4INSURANCE`, `NONE`, `CUSTOM`) |
| `custom_tax_amount`| DECIMAL(10,2)| NULLABLE | 직접 입력한 고액 공제금 |
| `has_holiday_pay` | BOOLEAN | DEFAULT FALSE| 주휴수당 자동 적용 여부 |
| `has_overtime_pay`| BOOLEAN | DEFAULT FALSE| 연장수당(1.5배) 자동 적용 여부 |
| `is_deleted` | BOOLEAN | DEFAULT FALSE| (Soft Delete) |
| `created_at` | TIMESTAMP | NOT NULL | 생성 일시 |
| `updated_at` | TIMESTAMP | NOT NULL | 수정 일시 |

### ⏳ 1.6 출퇴근 로깅 및 사비 지출 (Recording & Expenses)
#### `work_logs` (정밀 근무 기록 원장)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 근무 기록 식별자 |
| `workplace_id` | BIGINT | FK, NOT NULL | 속한 사업장 ID |
| `user_id` | BIGINT | FK, NOT NULL | 기록 유저 ID (대시보드 월별 차트 조회를 위해 인덱스 필수) |
| `start_time` | TIMESTAMP | NOT NULL | 출근 일시 |
| `end_time` | TIMESTAMP | NOT NULL | 퇴근 일시 |
| `rest_minutes` | INT | DEFAULT 30 | 체류된 휴게 시간 분 단위 (수동 조정 가능) |
| `status` | VARCHAR(20) | NOT NULL | 상태 태깅 (`NORMAL`, `LATE`, `EARLY_LEAVE`, `SICK`, `SUBSTITUTE`) |
| `total_calculated_wage` | DECIMAL(10,2) | NOT NULL | 이 날 번 일일 총급여 (대시보드 일간 차트용 캐싱 컬럼) |
| `memo` | TEXT | NULLABLE | 업무 특이사항 메모 |
| `receipt_image_id`| BIGINT | FK, NULLABLE | 업무용 법인카드 등 지출 '업무 영수증' 이미지 (`files` 참조) |
| `is_deleted` | BOOLEAN | DEFAULT FALSE| 삭제 여부 |
| `created_at` | TIMESTAMP | NOT NULL | 기록 일시 |

#### `expenses` (내 지갑 사비 지출 내역)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 지출 식별자 |
| `work_log_id` | BIGINT | FK, NOT NULL | 어느 날짜 근무 중에 발생한 사비인지 날짜 귀속 |
| `amount` | DECIMAL(10,2)| NOT NULL | 내 사비 지출액 (차트 **'순수익'** 계산 시 차감용 데이터) |
| `category` | VARCHAR(50) | NOT NULL | 분류 (`TRANSPORT`(교통비), `MEAL`(식비), `ETC`) |
| `description` | VARCHAR(255) | NULLABLE | 상세 내역 메모 |
| `expense_receipt_id`| BIGINT| FK, NULLABLE | 내 지갑에서 나간 사비 '증빙 영수증' 이미지 (`files` 참조) |

### 💰 1.7 통합 대시보드 캐시 및 정산표 (Period Settlements)
#### `period_settlements` (월별/주차별 통합 미수금 정산 보드)
> **[통폐합 다이어트 튜닝점]** 주급용(`weekly`)과 월급용(`monthly`) 테이블의 뼈대가 같으므로 파편화와 조인 낭비를 막기 위해 단일 테이블로 병합하고, `period_type`으로 대시보드 렌더링을 스위칭합니다. 무거운 DB 트리거를 대신하여, 향후 백엔드의 `@TransactionalEventListener`를 통해 비동기로 안전하게 금액 캐싱(동기화)이 이뤄질 예정입니다.

| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 / 역할 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK | 정산표 고유 식별자 |
| `workplace_id` | BIGINT | FK, NOT NULL | 귀속 사업장 |
| `user_id` | BIGINT | FK, NOT NULL | 소유 유저 식별자 |
| `period_type` | VARCHAR(20) | NOT NULL | 정산 기준 단위 (`WEEKLY` 또는 `MONTHLY`) |
| `period_value` | VARCHAR(20) | NOT NULL | 정산 대상 기간 (주간: `2026-W12`, 월간: `2026-03`) |
| `total_work_hours`| DECIMAL(10,2)| NOT NULL | 이 기간 총 근무 시간 (대시보드 요약 타일 캐시 데이터용) |
| `expected_wage` | DECIMAL(10,2)| NOT NULL | 비즈니스 로직(세법/지출 등) 계산을 모두 마친 이 기간의 총 **예상 순수익** |
| `is_holiday_pay_met`| BOOLEAN | DEFAULT FALSE| 주휴수당 발생 요건(15시간) 당첨/달성 여부 (주간 주급표 전용값) |
| `actual_deposit` | DECIMAL(10,2)| NULLABLE | 월급/주급 지급일 이후, 유저가 검증 입력한 '실제 통장 입금액' |
| `status` | VARCHAR(20) | NOT NULL | 입금/미수금 상태 (`WAITING`(대기/미수금), `COMPLETED`(입금완료)) |

*(참고: 1:1 조인 낭비를 유발하던 `user_notification_settings` 알림 테이블은 모(母)테이블인 `users` 컬럼 영역(`is_payday_warning_on` 등)으로 통폐합(흡수) 조치되었으며, `admin_content_logs` 등 부가 모듈은 앱 고도화 시 추가합니다)*

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