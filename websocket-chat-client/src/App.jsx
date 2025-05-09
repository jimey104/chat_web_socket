import React from 'react';
import { Routes, Route } from 'react-router-dom';
import ChatRoom from "./pages/ChatRoom.jsx";
import Home from "./pages/Home.jsx";

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/chat/:roomId" element={<ChatRoom />} />
    </Routes>
  );
};

export default App;
