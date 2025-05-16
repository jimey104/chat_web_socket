package com.example.chat_websocket.service;

import com.example.chat_websocket.entity.ChatMessage;
import com.example.chat_websocket.entity.StudyGroup;
import com.example.chat_websocket.repository.ChatMessageRepository;
import com.example.chat_websocket.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final StudyGroupRepository studyGroupRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<StudyGroup> getAllGroups() {
        return studyGroupRepository.findAll();
    }

    public Optional<StudyGroup> getStudyGroup(Long groupId) {
        return studyGroupRepository.findById(groupId);
    }

    public StudyGroup createStudyGroup(StudyGroup request) {
        request.setCreatedAt(LocalDateTime.now());
        if (request.getCurrentMember() == null) request.setCurrentMember(0);
        if (request.getStatus() == null) request.setStatus("모집중");
        return studyGroupRepository.save(request);
    }

    public List<ChatMessage> getMessages(Long groupId) {
        return chatMessageRepository.findByStudyGroupIdOrderByCreatedAt(groupId);
    }

    public Optional<ChatMessage> saveMessage(Long groupId, ChatMessage request) {
        return studyGroupRepository.findById(groupId).map(group -> {
            ChatMessage message = ChatMessage.builder()
                    .userId(request.getUserId())
                    .userName(request.getUserName())
                    .content(request.getContent())
                    .createdAt(request.getCreatedAt())
                    .studyGroup(group)
                    .build();
            return chatMessageRepository.save(message);
        });
    }

    public Optional<ChatMessage> getMessage(Long messageId) {
        return chatMessageRepository.findById(messageId);
    }

    public boolean deleteMessage(Long messageId) {
        if (!chatMessageRepository.existsById(messageId)) return false;
        chatMessageRepository.deleteById(messageId);
        return true;
    }
}
