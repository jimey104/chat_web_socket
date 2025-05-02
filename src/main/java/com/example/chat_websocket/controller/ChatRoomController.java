package com.example.chat_websocket.controller;

import com.example.chat_websocket.entity.ChatMessage;
import com.example.chat_websocket.entity.ChatRoom;
import com.example.chat_websocket.repository.ChatMessageRepository;
import com.example.chat_websocket.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // ✅ 전체 채팅방 목록 조회
    @GetMapping
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // 단일 채팅방 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable Long roomId) {
        return chatRoomRepository.findById(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{chatRoomName}")
    public ResponseEntity<ChatRoom> createChatRoom(@PathVariable String chatRoomName) {
        if (chatRoomName == null || chatRoomName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomName(chatRoomName);
        ChatRoom saved = chatRoomRepository.save(chatRoom);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            return ResponseEntity.notFound().build();
        }

        chatRoomRepository.deleteById(roomId);
        return ResponseEntity.noContent().build(); // 204
    }

    // 채팅방의 메시지 전체 조회
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long roomId) {
        return chatRoomRepository.findById(roomId)
                .map(chatRoom -> ResponseEntity.ok(chatRoom.getMessages()))
                .orElse(ResponseEntity.notFound().build());
    }

    // 방식 메시지 저장
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ChatMessage> sendMessage(
            @PathVariable Long roomId,
            @RequestBody ChatMessage request
    ) {
        return chatRoomRepository.findById(roomId).map(chatRoom -> {
            ChatMessage message = new ChatMessage();
            message.setUserId(request.getUserId());
            message.setUserName(request.getUserName());
            message.setContent(request.getContent());
            message.setCreatedAt(LocalDateTime.now());
            message.setChatRoom(chatRoom);

            ChatMessage saved = chatMessageRepository.save(message);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 단일 메시지 조회
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<ChatMessage> getMessage(@PathVariable Long messageId) {
        return chatMessageRepository.findById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 메시지 삭제
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        if (!chatMessageRepository.existsById(messageId)) {
            return ResponseEntity.notFound().build();
        }
        chatMessageRepository.deleteById(messageId);
        return ResponseEntity.noContent().build();
    }
}

