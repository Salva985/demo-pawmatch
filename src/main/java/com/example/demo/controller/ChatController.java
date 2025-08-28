package com.example.demo.controller;

import com.example.demo.dto.chat.ChatRequestDTO;
import com.example.demo.dto.chat.ChatResponseDTO;
import com.example.demo.service.interfaces.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService; }

    @GetMapping
    public ResponseEntity<List<ChatResponseDTO>> getAll() {
        return ResponseEntity.ok(chatService.getAllChats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponseDTO> getById(@PathVariable long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @PostMapping
    public ResponseEntity<ChatResponseDTO> create(@Valid @RequestBody ChatRequestDTO body) {
        ChatResponseDTO created = chatService.createChat(body);
        return ResponseEntity.created(URI.create("/api/chats/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }
}
