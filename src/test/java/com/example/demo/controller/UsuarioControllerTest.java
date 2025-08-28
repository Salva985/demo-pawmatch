package com.example.demo.controller;

import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.dto.usuario.UsuarioResponseDTO;
import com.example.demo.dto.usuario.UsuarioUpdateDTO;
import com.example.demo.service.interfaces.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    UsuarioService usuarioService;


    @Test
    @DisplayName("GET /api/usuarios -> 200 con lista de usuarios")
    void getAllUsuarios_ok() throws Exception {
        var u1 = resp(1L, "Lucía", "lucia@ejemplo.com", "Málaga");
        var u2 = resp(2L, "Carlos", "carlos@ejemplo.com", "Barcelona");

        when(usuarioService.getAllUsuarios()).thenReturn(List.of(u1,u2));

        mvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Lucía"))
                .andExpect(jsonPath("$[1].email").value("carlos@ejemplo.com"));

        verify(usuarioService).getAllUsuarios();
    }

    @Test
    @DisplayName("Get /api/usuarios/{id} -> 200 cuando existe")
    void getUsuarioById_ok() throws Exception {
        var user = resp(10L, "Lucía", "lucia@example.com", "Málaga");

        when(usuarioService.getUsuarioById(10L)).thenReturn(user);

        mvc.perform(get("/api/usuarios/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Lucía"));

        verify(usuarioService).getUsuarioById(10L);
    }

    @Test
    @DisplayName("POST /api/usuarios -> 201 Created + Location cuando el body es válido")
    void createUsuario_created_withLocation() throws Exception {
        var req = new UsuarioRequestDTO();
        req.setNombre("Lucía");
        req.setEmail("lucia@example.com");
        req.setPassword("12345678");
        req.setCiudad("Málaga");

        var created = resp(100L, "Lucía", "lucia@example.com", "Málaga");
        when(usuarioService.createUsuario(any())).thenReturn(created);

        var result = mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/usuarios/100"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andReturn();

        // Verifica que el body que recibió el servicio es el esperado
        ArgumentCaptor<UsuarioRequestDTO> captor = ArgumentCaptor.forClass(UsuarioRequestDTO.class);
        verify(usuarioService).createUsuario(captor.capture());
        var sent = captor.getValue();
        assertThat(sent.getNombre()).isEqualTo("Lucía");
        assertThat(sent.getEmail()).isEqualTo("lucia@example.com");
    }

    @Test
    @DisplayName("POST /api/usuarios -> 400 Bad Request si el body no cumple @Valid")
    void createUsuario_badRequest_whenInvalidBody() throws Exception {
        // email inválido y password corta (depende de tus constraints)
        var req = new UsuarioRequestDTO();
        req.setNombre(""); // NotBlank
        req.setEmail("luciaexample.com"); // Email inválido
        req.setPassword("123"); // Size min
        req.setCiudad("Málaga");

        mvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).createUsuario(any());
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} -> 200 OK cuando el body es válido")
    void updateUsuario_ok() throws Exception {
        var req = new UsuarioUpdateDTO();
        req.setNombre("Lucía G.");
        req.setCiudad("Sevilla");

        var updated = resp(5L, "Lucía G.", "lucia@example.com", "Sevilla");
        when(usuarioService.updateUsuario(eq(5L), any())).thenReturn(updated);

        mvc.perform(put("/api/usuarios/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Lucía G."))
                .andExpect(jsonPath("$.ciudad").value("Sevilla"));

        verify(usuarioService).updateUsuario(eq(5L), any());
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} -> 204 No Content")
    void deleteUsuario_noContent() throws Exception {
        doNothing().when(usuarioService).deleteUsuario(9L);

        mvc.perform(delete("/api/usuarios/{id}", 9))
                .andExpect(status().isNoContent());

        verify(usuarioService).deleteUsuario(9L);
    }

    // --------- HELPERS ------------
    private static UsuarioResponseDTO resp(Long id, String nombre, String email, String ciudad){
        var dto = new UsuarioResponseDTO();

        dto.setId(id);
        dto.setNombre(nombre);
        dto.setEmail(email);
        dto.setCiudad(ciudad);

        return dto;
    }
















}