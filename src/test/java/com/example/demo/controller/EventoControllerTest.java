package com.example.demo.controller;

import com.example.demo.dto.evento.EventoRequestDTO;
import com.example.demo.dto.evento.EventoResponseDTO;
import com.example.demo.dto.evento.EventoUpdateDTO;
import com.example.demo.service.interfaces.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
class EventoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    EventoService eventoService;

    private EventoResponseDTO resp(long id, String nombre, String ubicacion, String descripcion, LocalDateTime fecha) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setId(id);
        dto.setNombre(nombre);
        dto.setUbicacion(ubicacion);
        dto.setDescripcion(descripcion);
        dto.setFechaEvento(fecha);
        return dto;
    }

    @Test
    @DisplayName("GET /api/eventos -> 200 & lista")
    void getAll_ok() throws Exception {
        var e1 = resp(1L, "Paseo en el parque", "Madrid", "Plan canino", LocalDateTime.of(2030,5,10,10,30));
        var e2 = resp(2L, "Quedada playa", "Barcelona", "Arena y sol", LocalDateTime.of(2030,6,1,9,0));

        Mockito.when(eventoService.getAllEventos()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Paseo en el parque")))
                .andExpect(jsonPath("$[1].ubicacion", is("Barcelona")));
    }

    @Test
    @DisplayName("GET /api/eventos/{id} -> 200 & un evento")
    void getById_ok() throws Exception {
        var e = resp(5L, "Quedada monte", "Granada", "Ruta senderismo", LocalDateTime.of(2031,1,1,12,0));
        Mockito.when(eventoService.getEventoById(5L)).thenReturn(e);

        mockMvc.perform(get("/api/eventos/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.nombre", is("Quedada monte")))
                .andExpect(jsonPath("$.ubicacion", is("Granada")));
    }

    @Test
    @DisplayName("POST /api/eventos -> 201 & Location")
    void create_ok() throws Exception {
        EventoRequestDTO req = new EventoRequestDTO();
        req.setNombre("Tarde de juegos");
        req.setDescripcion("Juguetes y socialización");
        req.setUbicacion("Valencia");
        req.setFechaEvento(LocalDateTime.of(2031, 3, 15, 17, 0));

        var created = resp(99L, "Tarde de juegos", "Valencia", "Juguetes y socialización",
                LocalDateTime.of(2031, 3, 15, 17, 0));

        Mockito.when(eventoService.createEvento(any(EventoRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/eventos/99"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.nombre", is("Tarde de juegos")))
                .andExpect(jsonPath("$.ubicacion", is("Valencia")));
    }

    @Test
    @DisplayName("PUT /api/eventos/{id} -> 200 & actualizado")
    void update_ok() throws Exception {
        EventoUpdateDTO upd = new EventoUpdateDTO();
        upd.setNombre("Tarde de juegos (edit)");
        upd.setDescripcion("Descripción editada");
        upd.setUbicacion("Valencia Centro");
        upd.setFechaEvento(LocalDateTime.of(2031, 3, 16, 18, 0));

        var updated = resp(12L, "Tarde de juegos (edit)", "Valencia Centro", "Descripción editada",
                LocalDateTime.of(2031, 3, 16, 18, 0));

        Mockito.when(eventoService.updateEvento(eq(12L), any(EventoUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/eventos/{id}", 12L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(12)))
                .andExpect(jsonPath("$.nombre", is("Tarde de juegos (edit)")))
                .andExpect(jsonPath("$.ubicacion", is("Valencia Centro")));
    }

    @Test
    @DisplayName("DELETE /api/eventos/{id} -> 204")
    void delete_ok() throws Exception {
        Mockito.doNothing().when(eventoService).deleteEvento(7L);

        mockMvc.perform(delete("/api/eventos/{id}", 7L))
                .andExpect(status().isNoContent());
    }
}