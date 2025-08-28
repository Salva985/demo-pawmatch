package com.example.demo.controller;

import com.example.demo.dto.preferencias.PreferenciasRequestDTO;
import com.example.demo.dto.preferencias.PreferenciasResponseDTO;
import com.example.demo.dto.preferencias.PreferenciasUpdateDTO;
import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import com.example.demo.service.interfaces.PreferenciasService;
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

@WebMvcTest(PreferenciasController.class)
class PreferenciasControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    PreferenciasService preferenciasService;

    // ---- helper ----
    private PreferenciasResponseDTO resp(long id, Long usuarioId) {
        var dto = new PreferenciasResponseDTO();
        dto.setId(id);
        dto.setUsuarioId(usuarioId);
        dto.setRaza("Labrador");
        dto.setMedida(Medida.MEDIANO);
        dto.setEdadMin(2);
        dto.setEdadMax(8);
        dto.setCaracter(Caracter.SOCIAL);
        dto.setDistanciaMaxKm(20);
        return dto;
    }

    @Test
    @DisplayName("GET /api/preferencias -> 200 & lista")
    void getAll_ok() throws Exception {
        Mockito.when(preferenciasService.getAllPreferencias())
                .thenReturn(List.of(resp(1L, 10L), resp(2L, 11L)));

        mockMvc.perform(get("/api/preferencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].usuarioId", is(10)))
                .andExpect(jsonPath("$[0].medida", is("MEDIANO")))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    @DisplayName("GET /api/preferencias/{id} -> 200 & una")
    void getById_ok() throws Exception {
        Mockito.when(preferenciasService.getPreferenciasById(5L))
                .thenReturn(resp(5L, 77L));

        mockMvc.perform(get("/api/preferencias/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.usuarioId", is(77)))
                .andExpect(jsonPath("$.caracter", is("SOCIAL")));
    }

    @Test
    @DisplayName("GET /api/preferencias/usuario/{usuarioId} -> 200 & por usuario")
    void getByUsuario_ok() throws Exception {
        Mockito.when(preferenciasService.getPreferenciasByUsuarioId(99L))
                .thenReturn(resp(12L, 99L));

        mockMvc.perform(get("/api/preferencias/usuario/{usuarioId}", 99L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(12)))
                .andExpect(jsonPath("$.usuarioId", is(99)));
    }

    @Test
    @DisplayName("POST /api/preferencias -> 201 & Location")
    void create_ok() throws Exception {
        var req = new PreferenciasRequestDTO();
        req.setUsuarioId(50L);
        req.setRaza("Beagle");
        req.setMedida(Medida.PEQUENO);
        req.setEdadMin(1);
        req.setEdadMax(6);
        req.setCaracter(Caracter.JUGETON);
        req.setDistanciaMaxKm(15);

        var created = resp(123L, 50L);
        created.setRaza("Beagle");
        created.setMedida(Medida.PEQUENO);
        created.setEdadMin(1);
        created.setEdadMax(6);
        created.setCaracter(Caracter.JUGETON);
        created.setDistanciaMaxKm(15);

        Mockito.when(preferenciasService.createPreferencias(any(PreferenciasRequestDTO.class)))
                .thenReturn(created);

        mockMvc.perform(post("/api/preferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/preferencias/123"))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.usuarioId", is(50)))
                .andExpect(jsonPath("$.raza", is("Beagle")))
                .andExpect(jsonPath("$.medida", is("PEQUENO")))
                .andExpect(jsonPath("$.caracter", is("JUGETON")))
                .andExpect(jsonPath("$.distanciaMaxKm", is(15)));
    }

    @Test
    @DisplayName("PUT /api/preferencias/{id} -> 200 & actualizado")
    void update_ok() throws Exception {
        var upd = new PreferenciasUpdateDTO();
        upd.setRaza("Mestizo");
        upd.setMedida(Medida.GRANDE);
        upd.setEdadMin(3);
        upd.setEdadMax(10);
        upd.setCaracter(Caracter.TRANQUILO);
        upd.setDistanciaMaxKm(30);

        var updated = resp(7L, 70L);
        updated.setRaza("Mestizo");
        updated.setMedida(Medida.GRANDE);
        updated.setEdadMin(3);
        updated.setEdadMax(10);
        updated.setCaracter(Caracter.TRANQUILO);
        updated.setDistanciaMaxKm(30);

        Mockito.when(preferenciasService.updatePreferencias(eq(7L), any(PreferenciasUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/preferencias/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.usuarioId", is(70)))
                .andExpect(jsonPath("$.raza", is("Mestizo")))
                .andExpect(jsonPath("$.medida", is("GRANDE")))
                .andExpect(jsonPath("$.edadMin", is(3)))
                .andExpect(jsonPath("$.edadMax", is(10)))
                .andExpect(jsonPath("$.caracter", is("TRANQUILO")))
                .andExpect(jsonPath("$.distanciaMaxKm", is(30)));
    }

    @Test
    @DisplayName("DELETE /api/preferencias/{id} -> 204")
    void delete_ok() throws Exception {
        Mockito.doNothing().when(preferenciasService).deletePreferencias(4L);

        mockMvc.perform(delete("/api/preferencias/{id}", 4L))
                .andExpect(status().isNoContent());
    }
}