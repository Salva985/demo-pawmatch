package com.example.demo.controller;

import com.example.demo.dto.preferencias.PreferenciasRequestDTO;
import com.example.demo.dto.preferencias.PreferenciasResponseDTO;
import com.example.demo.dto.preferencias.PreferenciasUpdateDTO;
import com.example.demo.service.interfaces.PreferenciasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/preferencias")
public class PreferenciasController {

    private final PreferenciasService preferenciasService;
    public PreferenciasController(PreferenciasService preferenciasService) {
        this.preferenciasService = preferenciasService;
    }

    @GetMapping
    public ResponseEntity<List<PreferenciasResponseDTO>> getAll() {
        return ResponseEntity.ok(preferenciasService.getAllPreferencias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreferenciasResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(preferenciasService.getPreferenciasById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PreferenciasResponseDTO> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(preferenciasService.getPreferenciasByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<PreferenciasResponseDTO> create(@Valid @RequestBody PreferenciasRequestDTO body) {
        PreferenciasResponseDTO created = preferenciasService.createPreferencias(body);
        return ResponseEntity.created(URI.create("/api/preferencias/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferenciasResponseDTO> update(@PathVariable Long id,
                                                          @Valid @RequestBody PreferenciasUpdateDTO body) {
        return ResponseEntity.ok(preferenciasService.updatePreferencias(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preferenciasService.deletePreferencias(id);
        return ResponseEntity.noContent().build();
    }
}