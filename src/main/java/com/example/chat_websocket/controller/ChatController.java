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

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 클라이언트가 /app/chat/send 로 메시지를 보내면
     * 1. DB에 저장하고
     * 2. /topic/chat/{roomId} 경로로 구독 중인 클라이언트에게 브로드캐스트
     */
    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageDTO dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        ChatMessage message = ChatMessage.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .content(dto.getContent())
                .chatRoom(chatRoom)
                .createdAt(LocalDateTime.now())
                .build();

        chatMessageRepository.save(message);

        // 프론트에 DTO 형태로 다시 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/" + dto.getRoomId(), dto);
    }
}
