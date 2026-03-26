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