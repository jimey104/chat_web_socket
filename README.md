# 💬 WebSocket 기반 실시간 채팅 시스템

## 📌 프로젝트 개요

이 프로젝트는 **Spring Boot 3.4.4**와 **React + Vite**를 기반으로 하는 **실시간 채팅 시스템**입니다. WebSocket(STOMP + SockJS)을 사용하여 서버와 클라이언트 간 실시간 메시지 전송을 구현하고, 채팅방 및 메시지 데이터는 MySQL에 저장됩니다. 모든 채팅은 단일 페이지 내에서 처리되며, REST API와 WebSocket이 혼합되어 사용됩니다.

---

## ⚙️ 시스템 요구 사항

- **Java 17+**
- **Node.js 18+**
- **MySQL 8.x**
- **Yarn**

---

## 🧩 설치 및 실행 방법

### ✅ 백엔드

1. MySQL 설치 및 DB 생성
```sql
CREATE DATABASE chat_db;
```
2. application.yml 설정 (예시)
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chat_db
    username: root
    password: yourpassword

```

3. Spring Boot 실행
```bash
./gradlew bootRun
```

## 🛠️ 프론트엔드 실행을 위한 설치

1. 패키지 설치
```bash
yarn install
```
```bash
yarn add sockjs-client stompjs
```
```bash
yarn dev

```
## ✅ 현재 구현된 기능

- 채팅방 생성 / 목록 조회 / 삭제 (REST API)
- 채팅 메시지 전송 및 저장 (WebSocket + JPA)
- 채팅방 단일 페이지 내 선택 및 실시간 메시지 처리
- 하드코딩된 유저 정보 기반 채팅 기능 구현

---

## 🚧 추후 구현 예정 기능

- 다중 채팅방 동시 접속 및 UI 지원
- 유저 간 1:1 혹은 N:N 그룹 채팅 기능
- 채팅방 참여자 리스트 UI
- 메시지 읽음 상태 표시
- WebSocket 연결 상태 표시 및 재연결 처리
- 사용자 인증 및 로그인 기능 추가
