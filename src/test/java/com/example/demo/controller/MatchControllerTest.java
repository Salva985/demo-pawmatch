package com.example.demo.controller;

import com.example.demo.dto.match.MatchRequestDTO;
import com.example.demo.dto.match.MatchResponseDTO;
import com.example.demo.dto.match.MatchUpdateDTO;
import com.example.demo.enums.EstadoMatch;
import com.example.demo.service.interfaces.MatchService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    MatchService matchService;

    private MatchResponseDTO resp(long id, long origenId, long destinoId, EstadoMatch estado) {
        MatchResponseDTO dto = new MatchResponseDTO();
        dto.setId(id);
        dto.setMascotaOrigenId(origenId);
        dto.setMascotaDestinoId(destinoId);
        dto.setEstado(estado);
        // si tu DTO tiene fechaMatch, puedes setear una fija o dejar null
        return dto;
    }

    @Test
    @DisplayName("GET /api/matches -> 200 & lista")
    void getAll_ok() throws Exception {
        var m1 = resp(1L, 10L, 20L, EstadoMatch.PENDIENTE);
        var m2 = resp(2L, 11L, 21L, EstadoMatch.CONFIRMADO);

        Mockito.when(matchService.getAllMatches()).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].estado", is("PENDIENTE")))
                .andExpect(jsonPath("$[1].estado", is("CONFIRMADO")));
    }

    @Test
    @DisplayName("GET /api/matches/{id} -> 200 & uno")
    void getById_ok() throws Exception {
        var m = resp(7L, 33L, 44L, EstadoMatch.PENDIENTE);
        Mockito.when(matchService.getMatchById(7L)).thenReturn(m);

        mockMvc.perform(get("/api/matches/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.mascotaOrigenId", is(33)))
                .andExpect(jsonPath("$.mascotaDestinoId", is(44)))
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));
    }

    @Test
    @DisplayName("POST /api/matches -> 201 & Location")
    void create_ok() throws Exception {
        MatchRequestDTO req = new MatchRequestDTO();
        req.setMascotaOrigenId(50L);
        req.setMascotaDestinoId(60L);
        req.setEstado(EstadoMatch.PENDIENTE); // opcional según tu servicio

        var created = resp(99L, 50L, 60L, EstadoMatch.PENDIENTE);

        Mockito.when(matchService.createMatch(any(MatchRequestDTO.class)))
                .thenReturn(created);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/matches/99"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.mascotaOrigenId", is(50)))
                .andExpect(jsonPath("$.mascotaDestinoId", is(60)))
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));
    }

    @Test
    @DisplayName("PUT /api/matches/{id} -> 200 & actualizado")
    void update_ok() throws Exception {
        MatchUpdateDTO upd = new MatchUpdateDTO();
        upd.setEstado(EstadoMatch.CONFIRMADO);

        var updated = resp(5L, 70L, 80L, EstadoMatch.CONFIRMADO);

        Mockito.when(matchService.updateMatch(eq(5L), any(MatchUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/matches/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.estado", is("CONFIRMADO")));
    }

    @Test
    @DisplayName("DELETE /api/matches/{id} -> 204")
    void delete_ok() throws Exception {
        Mockito.doNothing().when(matchService).deleteMatch(4L);

        mockMvc.perform(delete("/api/matches/{id}", 4L))
                .andExpect(status().isNoContent());
    }
}