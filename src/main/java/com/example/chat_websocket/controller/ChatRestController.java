package com.example.chat_websocket.controller;

import com.example.chat_websocket.entity.ChatMessage;
import com.example.chat_websocket.entity.StudyGroup;
import com.example.chat_websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping
    public List<StudyGroup> getAllGroups() {
        return chatService.getAllGroups();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<StudyGroup> getStudyGroup(@PathVariable Long groupId) {
        return chatService.getStudyGroup(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudyGroup> create(@RequestBody StudyGroup request) {
        return ResponseEntity.ok(chatService.createStudyGroup(request));
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long groupId) {
        return chatService.getStudyGroup(groupId)
                .map(group -> ResponseEntity.ok(chatService.getMessages(groupId)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{groupId}/messages")
    public ResponseEntity<ChatMessage> sendMessage(
            @PathVariable Long groupId,
            @RequestBody ChatMessage request
    ) {
        return chatService.saveMessage(groupId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<ChatMessage> getMessage(@PathVariable Long messageId) {
        return chatService.getMessage(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        return chatService.deleteMessage(messageId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
