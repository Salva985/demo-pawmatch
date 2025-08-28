// src/test/java/com/example/demo/controller/MascotaControllerTest.java
package com.example.demo.controller;

import com.example.demo.dto.mascota.MascotaRequestDTO;
import com.example.demo.dto.mascota.MascotaResponseDTO;
import com.example.demo.dto.mascota.MascotaUpdateDTO;
import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import com.example.demo.enums.Sexo;
import com.example.demo.service.interfaces.MascotaService;
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

@WebMvcTest(MascotaController.class)
class MascotaControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    MascotaService mascotaService;

    private MascotaResponseDTO resp(long id, String nombre) {
        MascotaResponseDTO dto = new MascotaResponseDTO();
        dto.setId(id);
        dto.setNombre(nombre);
        dto.setEspecie("Perro");
        dto.setRaza("Mestizo");
        dto.setSexo(Sexo.MACHO);
        dto.setEdad(3);
        dto.setMedida(Medida.MEDIANO);
        dto.setCaracter(Caracter.JUGETON);
        dto.setDescripcion("Muy sociable");
        dto.setFotoUrl("http://foto.test/perrete.jpg");
        dto.setPropietarioId(42L);
        return dto;
    }

    @Test
    @DisplayName("GET /api/mascotas -> 200 & lista")
    void getAll_ok() throws Exception {
        var m1 = resp(1L, "Toby");
        var m2 = resp(2L, "Luna");

        Mockito.when(mascotaService.getAllMascotas()).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Toby")))
                .andExpect(jsonPath("$[1].nombre", is("Luna")));
    }

    @Test
    @DisplayName("GET /api/mascotas/{id} -> 200 & una mascota")
    void getById_ok() throws Exception {
        var m = resp(10L, "Kira");
        Mockito.when(mascotaService.getMascotaById(10L)).thenReturn(m);

        mockMvc.perform(get("/api/mascotas/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.nombre", is("Kira")))
                .andExpect(jsonPath("$.especie", is("Perro")));
    }

    @Test
    @DisplayName("POST /api/mascotas -> 201 & Location")
    void create_ok() throws Exception {
        MascotaRequestDTO req = new MascotaRequestDTO();
        req.setNombre("Boby");
        req.setEspecie("Perro");
        req.setRaza("Mestizo");
        req.setSexo(Sexo.MACHO);
        req.setEdad(2);
        req.setMedida(Medida.PEQUENO);
        req.setCaracter(Caracter.ACTIVO);
        req.setDescripcion("Le encanta correr");
        req.setFotoUrl("http://img.test/boby.jpg");
        req.setPropietarioId(5L);

        var created = resp(99L, "Boby");
        created.setEspecie("Perro");
        created.setMedida(Medida.PEQUENO);
        created.setCaracter(Caracter.ACTIVO);
        created.setPropietarioId(5L);

        Mockito.when(mascotaService.createMascota(any(MascotaRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/mascotas/99"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.nombre", is("Boby")))
                .andExpect(jsonPath("$.propietarioId", is(5)));
    }

    @Test
    @DisplayName("PUT /api/mascotas/{id} -> 200 & actualizado")
    void update_ok() throws Exception {
        MascotaUpdateDTO upd = new MascotaUpdateDTO();
        upd.setNombre("Boby Jr");
        upd.setEdad(3);
        upd.setDescripcion("Más tranquilo");

        var updated = resp(7L, "Boby Jr");
        updated.setEdad(3);
        updated.setDescripcion("Más tranquilo");

        Mockito.when(mascotaService.updateMascota(eq(7L), any(MascotaUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/mascotas/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.nombre", is("Boby Jr")))
                .andExpect(jsonPath("$.edad", is(3)))
                .andExpect(jsonPath("$.descripcion", is("Más tranquilo")));
    }

    @Test
    @DisplayName("DELETE /api/mascotas/{id} -> 204")
    void delete_ok() throws Exception {
        Mockito.doNothing().when(mascotaService).deleteMascota(4L);

        mockMvc.perform(delete("/api/mascotas/{id}", 4L))
                .andExpect(status().isNoContent());
    }
}