import React from 'react';
import { Routes, Route } from 'react-router-dom';
import ChatRoom from './pages/ChatRoom';
import Home from './pages/Home';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/chat/:roomId" element={<ChatRoom />} />
    </Routes>
  );
};

export default App;
