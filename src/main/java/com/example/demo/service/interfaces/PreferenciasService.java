// src/main/java/com/example/demo/service/interfaces/PreferenciasService.java
package com.example.demo.service.interfaces;

import com.example.demo.dto.preferencias.PreferenciasRequestDTO;
import com.example.demo.dto.preferencias.PreferenciasResponseDTO;
import com.example.demo.dto.preferencias.PreferenciasUpdateDTO;

import java.util.List;

public interface PreferenciasService {
    List<PreferenciasResponseDTO> getAllPreferencias();

    PreferenciasResponseDTO getPreferenciasById(Long id);

    PreferenciasResponseDTO getPreferenciasByUsuarioId(Long usuarioId);

    PreferenciasResponseDTO createPreferencias(PreferenciasRequestDTO request);

    PreferenciasResponseDTO updatePreferencias(Long id, PreferenciasUpdateDTO update);

    void deletePreferencias(Long id);
}