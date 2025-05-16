package com.example.chat_websocket.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long groupId;
    private String userEmail;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
}
