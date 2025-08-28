package com.example.demo.service.interfaces;


import com.example.demo.dto.chat.ChatRequestDTO;
import com.example.demo.dto.chat.ChatResponseDTO;

import java.util.List;

public interface ChatService {
    List<ChatResponseDTO> getAllChats();

    ChatResponseDTO getChatById(Long id);

    ChatResponseDTO createChat(ChatRequestDTO chatRequest);

    void deleteChat(Long id);
}
