package com.example.demo.controller;

import com.example.demo.dto.chat.ChatRequestDTO;
import com.example.demo.dto.chat.ChatResponseDTO;
import com.example.demo.service.interfaces.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockitoBean
    private ChatService chatService;

    // ---- helpers ----
    private ChatResponseDTO chat(Long id, Long u1, Long u2) {
        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setId(id);
        dto.setUsuario1Id(u1);
        dto.setUsuario2Id(u2);
        return dto;
    }

    @Test
    @DisplayName("GET /api/chats -> 200 y lista")
    void getAll_ok() throws Exception {
        Mockito.when(chatService.getAllChats())
                .thenReturn(List.of(chat(1L, 10L, 20L), chat(2L, 11L, 21L)));

        mvc.perform(get("/api/chats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].usuario1Id", is(10)))
                .andExpect(jsonPath("$[0].usuario2Id", is(20)));
    }

    @Test
    @DisplayName("GET /api/chats/{id} -> 200 cuando existe")
    void getById_ok() throws Exception {
        Mockito.when(chatService.getChatById(5L))
                .thenReturn(chat(5L, 100L, 200L));

        mvc.perform(get("/api/chats/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.usuario1Id", is(100)))
                .andExpect(jsonPath("$.usuario2Id", is(200)));
    }

    @Test
    @DisplayName("POST /api/chats -> 201 Created + Location")
    void create_ok() throws Exception {
        ChatRequestDTO req = new ChatRequestDTO();
        req.setUsuario1Id(10L);
        req.setUsuario2Id(20L);

        ChatResponseDTO created = chat(99L, 10L, 20L);

        Mockito.when(chatService.createChat(any(ChatRequestDTO.class)))
                .thenReturn(created);

        mvc.perform(post("/api/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/chats/99"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.usuario1Id", is(10)))
                .andExpect(jsonPath("$.usuario2Id", is(20)));
    }

    @Test
    @DisplayName("DELETE /api/chats/{id} -> 204 No Content")
    void delete_ok() throws Exception {
        Mockito.doNothing().when(chatService).deleteChat(7L);

        mvc.perform(delete("/api/chats/{id}", 7))
                .andExpect(status().isNoContent());
    }
}