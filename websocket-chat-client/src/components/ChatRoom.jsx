import React, { useEffect, useState, useRef } from 'react';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';

const ChatRoom = () => {
  const user = {
    id: '123',
    name: '홍길동'
  };

  const [stompClient, setStompClient] = useState(null);
  const [chatMessages, setChatMessages] = useState([]);
  const [message, setMessage] = useState('');
  const [roomId, setRoomId] = useState('1');
  const connectedRef = useRef(false);
  const messageEndRef = useRef(null); // ✅ 자동 스크롤 기준점

  useEffect(() => {
    if (connectedRef.current) return;

    const socket = new SockJS('http://localhost:8788/ws-chat');
    const client = over(socket);

    client.connect({}, () => {
      console.log('✅ STOMP 연결됨');
      client.subscribe(`/topic/chat/${roomId}`, (msg) => {
        console.log('📩 메시지 수신');
        const body = JSON.parse(msg.body);

        const now = new Date();
        const timestamp = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

        setChatMessages(prev => [...prev, `[${timestamp}] ${body.userName}: ${body.content}`]);
      });
    });

    setStompClient(client);
    connectedRef.current = true;
  }, [roomId]);

  // ✅ 자동 스크롤
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
    if (e.key === 'Enter') {
      if (e.shiftKey) return;
      e.preventDefault();
      sendMessage();
    }
  };

  return (
    <div>
      <h2>채팅방 #{roomId}</h2>
      <textarea
        value={message}
        onChange={e => setMessage(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="메시지 입력 (Enter 전송, Shift+Enter 줄바꿈)"
        rows={2}
        style={{
          width: '300px',
          resize: 'none',
          padding: '8px',
          marginTop: '8px',
          borderRadius: '4px',
        }}
      />
      <br />
      <button onClick={sendMessage}>전송</button>

      {/* ✅ 자동 스크롤 가능한 영역 */}
      <div style={{ maxHeight: '300px', overflowY: 'auto', marginTop: '12px' }}>
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {chatMessages.map((msg, idx) => (
            <li key={idx}>{msg}</li>
          ))}
          <div ref={messageEndRef} />
        </ul>
      </div>
    </div>
  );
};

export default ChatRoom;
