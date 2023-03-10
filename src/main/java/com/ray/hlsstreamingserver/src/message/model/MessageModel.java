package com.ray.hlsstreamingserver.src.message.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @Setter
@AllArgsConstructor @NoArgsConstructor
public class MessageModel {
    private String type;
    private String sender;
    private String roomCode;
    private String message;
    private LocalDateTime timestamp;

}
