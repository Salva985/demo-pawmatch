package com.example.demo.controller;

import com.example.demo.dto.mascota.MascotaRequestDTO;
import com.example.demo.dto.mascota.MascotaResponseDTO;
import com.example.demo.dto.mascota.MascotaUpdateDTO;
import com.example.demo.service.interfaces.MascotaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    public MascotaController(MascotaService mascotaService) { this.mascotaService = mascotaService; }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> getAll() {
        return ResponseEntity.ok(mascotaService.getAllMascotas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.getMascotaById(id));
    }

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> create(@Valid @RequestBody MascotaRequestDTO body) {
        MascotaResponseDTO created = mascotaService.createMascota(body);
        return ResponseEntity.created(URI.create("/api/mascotas/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody MascotaUpdateDTO body) {
        return ResponseEntity.ok(mascotaService.updateMascota(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mascotaService.deleteMascota(id);
        return ResponseEntity.noContent().build();
    }
}