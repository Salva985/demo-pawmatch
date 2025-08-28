package com.example.demo.service.impl;

import com.example.demo.dto.chat.ChatRequestDTO;
import com.example.demo.dto.chat.ChatResponseDTO;
import com.example.demo.model.Chat;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<ChatResponseDTO> getAllChats() {
        return chatRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ChatResponseDTO getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Chat no encontrado con id: " + id));
        return mapToResponseDTO(chat);
    }

    @Override
    public ChatResponseDTO createChat(ChatRequestDTO chatRequest) {
        Usuario usuario1 = usuarioRepository.findById(chatRequest.getUsuario1Id())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado con id: " + chatRequest.getUsuario1Id()));
        Usuario usuario2 = usuarioRepository.findById(chatRequest.getUsuario2Id())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado con id: " + chatRequest.getUsuario2Id()));

        Chat chat = new Chat();
        chat.setUsuario1(usuario1);
        chat.setUsuario2(usuario2);

        return mapToResponseDTO(chatRepository.save(chat));
    }

    @Override
    public void deleteChat(Long id) {
        if(!chatRepository.existsById(id)){
            throw new RuntimeException("Chat no encontrado con id: " + id);
        }
        chatRepository.deleteById(id);
    }


    // Métodos auxiliares
    private ChatResponseDTO mapToResponseDTO(Chat chat){
        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setId(chat.getId());
        dto.setUsuario1Id(chat.getUsuario1().getId());
        dto.setUsuario2Id(chat.getUsuario2().getId());
        return dto;
    }
}
