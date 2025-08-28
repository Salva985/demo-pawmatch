package com.example.demo.controller;

import com.example.demo.dto.mensaje.MensajeRequestDTO;
import com.example.demo.dto.mensaje.MensajeResponseDTO;
import com.example.demo.dto.mensaje.MensajeUpdateDTO;
import com.example.demo.enums.EstadoMensaje;
import com.example.demo.service.interfaces.MensajeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MensajeController.class)
class MensajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // auto-configured by Spring Boot Test

    @MockitoBean
    private MensajeService mensajeService;

    private MensajeResponseDTO sample(Long id) {
        MensajeResponseDTO dto = new MensajeResponseDTO();
        dto.setId(id);
        dto.setChatId(10L);
        dto.setRemitenteId(20L);
        dto.setContenido("Hola!");
        dto.setEstado(EstadoMensaje.ENVIADO);
        dto.setFechaEnvio(LocalDateTime.of(2025, 1, 1, 12, 0));
        return dto;
    }

    @Test
    @DisplayName("GET /api/mensajes -> 200 & list")
    void getAll_ok() throws Exception {
        Mockito.when(mensajeService.getAllMensajes())
                .thenReturn(List.of(sample(1L), sample(2L)));

        mockMvc.perform(get("/api/mensajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].contenido", is("Hola!")));
    }

    @Test
    @DisplayName("GET /api/mensajes/{id} -> 200 & one item")
    void getById_ok() throws Exception {
        Mockito.when(mensajeService.getMensajeById(1L)).thenReturn(sample(1L));

        mockMvc.perform(get("/api/mensajes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.chatId", is(10)));
    }

    @Test
    @DisplayName("GET /api/mensajes/chat/{chatId} -> 200 & list by chat")
    void getByChat_ok() throws Exception {
        Mockito.when(mensajeService.getMensajesByChat(10L))
                .thenReturn(List.of(sample(1L)));

        mockMvc.perform(get("/api/mensajes/chat/{chatId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].chatId", is(10)));
    }

    @Test
    @DisplayName("POST /api/mensajes -> 201 & Location header")
    void create_ok() throws Exception {
        MensajeRequestDTO req = new MensajeRequestDTO();
        req.setChatId(10L);
        req.setRemitenteId(20L);
        req.setContenido("Nuevo mensaje");
        req.setEstado(EstadoMensaje.ENVIADO);

        MensajeResponseDTO created = sample(99L);
        created.setContenido("Nuevo mensaje");

        Mockito.when(mensajeService.createMensaje(any(MensajeRequestDTO.class)))
                .thenReturn(created);

        mockMvc.perform(post("/api/mensajes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/mensajes/99"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.contenido", is("Nuevo mensaje")));
    }

    @Test
    @DisplayName("PUT /api/mensajes/{id} -> 200 & updated")
    void update_ok() throws Exception {
        MensajeUpdateDTO upd = new MensajeUpdateDTO();
        upd.setContenido("Editado");
        upd.setEstado(EstadoMensaje.LEIDO);

        MensajeResponseDTO updated = sample(5L);
        updated.setContenido("Editado");
        updated.setEstado(EstadoMensaje.LEIDO);

        Mockito.when(mensajeService.updateMensaje(eq(5L), any(MensajeUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/mensajes/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.estado", is("LEIDO")))
                .andExpect(jsonPath("$.contenido", is("Editado")));
    }

    @Test
    @DisplayName("DELETE /api/mensajes/{id} -> 204")
    void delete_ok() throws Exception {
        Mockito.doNothing().when(mensajeService).deleteMensaje(7L);

        mockMvc.perform(delete("/api/mensajes/{id}", 7L))
                .andExpect(status().isNoContent());
    }
}