// ✅ StudyGroupDetail.jsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import ChatRoom from '../components/ChatRoom';
import api from '../api/api';

const StudyGroupDetail = () => {
  const { groupId } = useParams();
  const [group, setGroup] = useState(null);
  const [userEmail, setUserEmail] = useState('test1@test.ts');
  const [userName, setUserName] = useState('홍길동');

  
  useEffect(() => {
    api.get(`/api/group/chat/${groupId}`).then((res) => setGroup(res.data));
  }, [groupId]);

  if (!group) return <div>로딩 중...</div>;

  return (
    <div style={{ padding: 20 }}>
      <h1>📚 스터디 그룹 상세</h1>
      <h2>{group.title}</h2>
      <p>{group.description}</p>
      <p>👥 인원: {group.currentMember} / {group.maxMember}</p>
      <p>📅 생성일: {new Date(group.createdAt).toLocaleString()}</p>
      <p>📌 상태: {group.status}</p>

      <hr />
      <ChatRoom groupId={groupId} userEmail={userEmail} userName={userName} />
    </div>
  );
};

export default StudyGroupDetail;
