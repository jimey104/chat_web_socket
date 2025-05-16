package com.example.chat_websocket.controller;

import com.example.chat_websocket.dto.ChatMessageDTO;
import com.example.chat_websocket.entity.ChatMessage;
import com.example.chat_websocket.entity.StudyGroup;
import com.example.chat_websocket.repository.ChatMessageRepository;
import com.example.chat_websocket.repository.StudyGroupRepository;
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
    private final StudyGroupRepository studyGroupRepository;

    @MessageMapping("/chat/study-group/{groupId}")
    public void sendMessage(ChatMessageDTO dto) {
        StudyGroup group = studyGroupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new RuntimeException("스터디 그룹이 존재하지 않습니다."));

        dto.setCreatedAt(LocalDateTime.now());

        ChatMessage message = ChatMessage.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .content(dto.getContent())
                .studyGroup(group)
                .createdAt(dto.getCreatedAt())
                .build();

        chatMessageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/chat/study-group/" + dto.getGroupId(), dto);
    }
}