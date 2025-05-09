import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axios from 'axios';

const ChatRoom = () => {
  const { roomId } = useParams();
  const [messages, setMessages] = useState([]);
  const [content, setContent] = useState('');
  const stompRef = useRef(null);
  const userId = 2; // 임시 유저 ID
  const userName = 'aasd'; // 임시 유저 이름

  useEffect(() => {
    // ✅ 초기 메시지 조회
    axios.get(`/chatrooms/${roomId}/messages`).then((res) => setMessages(res.data));

    // ✅ WebSocket 연결
    const socket = new SockJS('http://localhost:8788/ws');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('✅ WebSocket 연결 성공');

        // ✅ 구독 설정
        stompClient.subscribe(`/topic/chat/${roomId}`, (msg) => {
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body]);
        });
      },
    });
    stompClient.activate();
    stompRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
  }, [roomId]);

  const handleSend = () => {
    if (!content.trim() || !stompRef.current?.connected) return;

    const dto = {
      userId,
      userName,
      content,
      roomId, // ✅ DTO에 맞춰 정확히 전달
    };

    console.log('📤 전송할 메시지 DTO:', dto);

    stompRef.current.publish({
      destination: `/app/chat/send`, // ✅ 서버 ChatController에 맞춘 경로
      body: JSON.stringify(dto),
    });

    setContent('');
  };

  return (
    <div style={{ padding: 20 }}>
      <h2>💬 채팅방 ID: {roomId}</h2>
      <div style={{ minHeight: 300, marginBottom: 20 }}>
        {messages.map((msg, i) => (
          <div key={msg.id || i}>
            <strong>{msg.userName}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <input
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="메시지를 입력하세요"
      />
      <button onClick={handleSend}>보내기</button>
    </div>
  );
};

export default ChatRoom;
