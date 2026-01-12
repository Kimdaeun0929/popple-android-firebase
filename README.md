# POPPLE
📍 위치 기반 팝업스토어 정보 공유 Android 애플리케이션

POPPLE은 **MZ세대(20~30대)**를 타겟으로  
현재 운영 중인 팝업스토어 정보를 지도 기반으로 제공하고,  
방문자 간 정보를 공유할 수 있도록 설계한 **모바일 애플리케이션**입니다.

---

## 📽 데모 영상
🎥 실제 앱 동작 시연 영상 (카카오TV)  
https://tv.kakao.com/v/447395076

---

## 🏆 수상 내역
- 교내 캡스톤 디자인 경진대회 **장려상 수상**

---

## 📌 프로젝트 개요
- 개발 형태: Android 앱 팀 프로젝트 (캡스톤 디자인)
- 개발 기간: 2024.03 ~ 2024.06
- 개발 목적
  - 흩어져 있는 팝업스토어 정보를 한 곳에서 제공
  - 방문 전에는 알 수 없는 현장 정보를 사용자 간 공유

---

## 🔍 주요 기능

### 🗺️ 팝업스토어 탐색 & 지도
- 현재 운영 중인 팝업스토어 목록 제공
- 지도 기반 마커 표시
- 마커 클릭 시 상세 정보 화면 이동
- 위치, 운영 기간, 설명, SNS 링크 확인 가능

---

### ❤️ 위시리스트 시스템
- 관심 팝업스토어 하트 클릭으로 저장
- 사용자별 위시리스트 관리
- Firebase Realtime Database에 userId 기준 저장
- 위시리스트 화면에서 저장 목록 확인 가능

---

### 💬 실시간 채팅 (프로젝트 기능)
- 팝업스토어별 채팅 공간 제공
- 방문자가 남긴 메시지를 실시간으로 확인 가능
- Firebase Realtime Database 기반 실시간 데이터 반영
- 팀 프로젝트 내 **다른 팀원이 구현한 기능**

---

## 🧱 데이터 구조 설계 (Firebase Realtime Database)

```text
users/
 └ userId/
    └ wishlist/
       └ popupStoreId: true

popupStores/
 └ popupStoreId/
    ├ title
    ├ location
    ├ date
    ├ description
    ├ instagram
    └ website

chats/
 └ popupStoreId/
    ├ messages/
    │   └ messageId/
    │      ├ message
    │      ├ nickname
    │      └ timestamp

```
- 사용자 ID 기준 데이터 분리
- 팝업스토어 단위 데이터 관리
- 실시간 데이터 반영을 고려한 NoSQL 구조

---

## ⚙️ 기술 스택
- 플랫폼: Android
- 언어: Java, XML
- 백엔드: Firebase Realtime Database
- 인증: Firebase Authentication
- 지도: 지도 API
- 디자인: Figma
- 협업 도구: Git, GitHub
- 개발 환경: Android Studio

---

## 👥 팀 구성 및 역할
### 👩‍💻 김다은 ([@kimdaeun0929](https://github.com/kimdaeun0929))
- 팀장 / PM
- 프로젝트 일정 관리 및 역할 분담
- 요구사항 분석 및 기능 우선순위 설정
- Firebase Realtime Database 전체 구조 설계
- 사용자 및 팝업스토어 데이터 관리 로직 구현
- 위시리스트 기능 구현
- GitHub 기반 협업 관리

### 👥 기타 팀원들
- XML 기반 UI 구현
- 로그인 기능 구현
- 지도 API 연동 및 지도 화면 구현
- 위치 기반 마커 표시 및 지도 상 이벤트 처리
- 채팅 화면 레이아웃 구성
- 실시간 채팅 기능 구현
- 채팅 메시지 송수신 처리
- 채팅 관련 Firebase 연동 로직 구현

---

## 🧠 기술적 고민 및 해결 (김다은 담당 기준)
🔹사용자별 데이터 관리
- Firebase Authentication을 통해 userId 식별
- userId 기준 위시리스트 데이터 분리 저장

🔹위시리스트 중복 저장 문제
- popupStoreId 기준 토글 방식 적용
- 중복 데이터 저장 방지 로직 구현

🔹데이터 구조 설계
- NoSQL 특성을 고려한 계층형 데이터 구조 설계
- 읽기/쓰기 효율을 고려한 데이터 분리

---

## 🎯 프로젝트 목적
본 프로젝트는 다음을 목표로 진행한 캡스톤 디자인 팀 프로젝트입니다.
- Android 앱 구조 이해
- Firebase Realtime Database 활용
- 사용자 기반 데이터 설계 경험
- 팀 프로젝트 협업 및 역할 분담 경험

---

## 🚀 향후 개선 사항
- 사용자 리뷰 및 평점 기능 추가
- 푸시 알림 기능 도입
- 관리자 페이지를 통한 팝업스토어 관리
- Firestore 기반 데이터 구조로 확장 검토

