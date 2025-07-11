# 🚀 WebSocket 기반 실시간 채팅 보일러플레이트

## 📌 개요
이 보일러플레이트는 **Spring Boot 3.4.4**와 **React + Vite**를 활용해  
WebSocket(STOMP + SockJS) 기반 실시간 채팅 기능을 손쉽게 구현할 수 있도록 구성되었습니다.

![실시간 채팅 데모](./images/chat.png)

📦 **주요 기능**
- 채팅방 생성/삭제/조회 (REST API)
- 메시지 전송 및 실시간 업데이트 (WebSocket + JPA)
- 단일 페이지 내 채팅방 선택 및 UI 반영

---

## 시스템 아키텍처 구성도

![구성도](./images/schema.png)

## ERD

![ERD](./images/ERD.png)

## ⚙️ 설치 및 실행

### ✅ 백엔드

```bash
# 서버 실행
./gradlew bootRun
```

### ✅ 프론트엔드
```bash
# 패키지 설치
yarn install
```

```bash
# 라이브러리 추가 (SockJS, STOMP)
yarn add sockjs-client stompjs
```

```bash
# 개발 서버 실행
yarn dev
```

---

## 🧩 주요 컴포넌트 구조

### **Backend (`Spring Boot`)**
- `ChatController.java` – REST API 엔드포인트
- `ChatMessage.java` – 메시지 엔티티 (JPA 활용)
- `ChatService.java` – 메시지 저장 및 WebSocket 로직

### **Frontend (`React + Vite`)**
- `ChatRoomList.jsx` – 채팅방 목록 조회 및 선택
- `ChatBox.jsx` – 메시지 입력 및 실시간 채팅 UI

---

## 🚀 확장 가능 기능
- 다중 채팅방 접속 및 UI 개선
- 메시지 읽음 상태 및 알림 표시
- 사용자 인증 및 로그인 기능 추가
- WebSocket 자동 재연결 처리

