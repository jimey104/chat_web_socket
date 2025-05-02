import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';
import '../styles/chatroom.css'; // ✅ 일반 CSS import

const ChatRoom = () => {
  const { roomId } = useParams();
  const user = { id: '123', name: '홍길동' };

  const [stompClient, setStompClient] = useState(null);
  const [chatMessages, setChatMessages] = useState([]);
  const [message, setMessage] = useState('');
  const connectedRef = useRef(false);
  const messageEndRef = useRef(null);

  useEffect(() => {
    if (connectedRef.current) return;

    const socket = new SockJS('http://localhost:8788/ws-chat');
    const client = over(socket);

    client.connect({}, () => {
      client.subscribe(`/topic/chat/${roomId}`, (msg) => {
        const body = JSON.parse(msg.body);
        const now = new Date();
        const timestamp = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;
        setChatMessages(prev => [...prev, `[${timestamp}] ${body.userName}: ${body.content}`]);
      });
    });

    setStompClient(client);
    connectedRef.current = true;
  }, [roomId]);

  useEffect(() => {
    if (messageEndRef.current) {
      messageEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [chatMessages]);

  const sendMessage = () => {
    if (stompClient && message.trim() !== '') {
      stompClient.send('/app/chat/send', {}, JSON.stringify({
        roomId,
        userId: user.id,
        userName: user.name,
        content: message
      }));
      setMessage('');
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  return (
    <div className="chat-room-container">
      <h2>채팅방 #{roomId}</h2>

      <div className="chat-messages">
        <ul>
          {chatMessages.map((msg, idx) => (
            <li key={idx}>{msg}</li>
          ))}
          <div ref={messageEndRef} />
        </ul>
      </div>

      <textarea
        className="chat-input"
        value={message}
        onChange={e => setMessage(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="메시지 입력 (Enter 전송, Shift+Enter 줄바꿈)"
        rows={2}
      />
      <button onClick={sendMessage}>전송</button>
    </div>
  );
};

export default ChatRoom;
