package com.example.demo.controller;

import com.example.demo.dto.mensaje.MensajeRequestDTO;
import com.example.demo.dto.mensaje.MensajeResponseDTO;
import com.example.demo.dto.mensaje.MensajeUpdateDTO;
import com.example.demo.service.interfaces.MensajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;
    public MensajeController(MensajeService mensajeService) { this.mensajeService = mensajeService; }

    @GetMapping
    public ResponseEntity<List<MensajeResponseDTO>> getAll() {
        return ResponseEntity.ok(mensajeService.getAllMensajes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mensajeService.getMensajeById(id));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MensajeResponseDTO>> getByChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(mensajeService.getMensajesByChat(chatId));
    }

    @PostMapping
    public ResponseEntity<MensajeResponseDTO> create(@Valid @RequestBody MensajeRequestDTO body) {
        MensajeResponseDTO created = mensajeService.createMensaje(body);
        return ResponseEntity.created(URI.create("/api/mensajes/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponseDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody MensajeUpdateDTO body) {
        return ResponseEntity.ok(mensajeService.updateMensaje(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mensajeService.deleteMensaje(id);
        return ResponseEntity.noContent().build();
    }
}