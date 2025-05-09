import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Home = () => {
  const [rooms, setRooms] = useState([]);
  const [newRoomName, setNewRoomName] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('/chatrooms').then((res) => {
      console.log('chatrooms 응답:', res.data); // 여기 추가
      setRooms(res.data);
    });
  }, []);
  

  const handleCreateRoom = async () => {
    if (!newRoomName.trim()) return;
    const res = await axios.post(`/chatrooms/${newRoomName}`);
    setRooms((prev) => [...prev, res.data]);
    console.log(rooms);
    setNewRoomName('');

  };

  return (
    <div style={{ padding: 20 }}>
      <h2>💬 채팅방 목록</h2>
      <ul>
        {rooms.map((room) => (
          <li key={room.id}>
            <button onClick={() => navigate(`/chat/${room.id}`)}>
              {room.chatRoomName}
            </button>
          </li>
        ))}
      </ul>
      <input
        value={newRoomName}
        onChange={(e) => setNewRoomName(e.target.value)}
        placeholder="새 채팅방 이름"
      />
      <button onClick={handleCreateRoom}>생성</button>
    </div>
  );
};

export default Home;
