package com.example.chat_websocket.controller;

import com.example.chat_websocket.dto.ChatMessageDTO;
import com.example.chat_websocket.entity.ChatMessage;
import com.example.chat_websocket.entity.ChatRoom;
import com.example.chat_websocket.repository.ChatMessageRepository;
import com.example.chat_websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageDTO dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        ChatMessage message = ChatMessage.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .content(dto.getContent())
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/chat/" + dto.getRoomId(), dto);
    }
}
