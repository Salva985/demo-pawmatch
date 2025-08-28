package com.example.demo.service.impl;

import com.example.demo.dto.preferencias.PreferenciasRequestDTO;
import com.example.demo.dto.preferencias.PreferenciasResponseDTO;

import com.example.demo.dto.preferencias.PreferenciasUpdateDTO;
import com.example.demo.model.Preferencias;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PreferenciasRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.interfaces.PreferenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PreferenciasServiceImpl implements PreferenciasService {

    @Autowired
    private PreferenciasRepository preferenciasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<PreferenciasResponseDTO> getAllPreferencias() {
        return preferenciasRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PreferenciasResponseDTO getPreferenciasById(Long id) {
        Preferencias p = preferenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preferencias no encontradas con id: " + id));
        return toResponse(p);
    }

    @Override
    public PreferenciasResponseDTO getPreferenciasByUsuarioId(Long usuarioId) {
        Preferencias p = preferenciasRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new RuntimeException("Preferencias no encontradas para el usuario id: " + usuarioId));
        return toResponse(p);
    }

    @Override
    public PreferenciasResponseDTO createPreferencias(PreferenciasRequestDTO request) {
        if (request.getUsuarioId() == null) {
            throw new RuntimeException("usuarioId es obligatorio para crear preferencias.");
        }

        if (preferenciasRepository.existsByUsuario_Id(request.getUsuarioId())) {
            throw new RuntimeException("El usuario ya tiene preferencias asociadas.");
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + request.getUsuarioId()));

        validarRangoEdades(request.getEdadMin(), request.getEdadMax());

        Preferencias p = new Preferencias();
        p.setRaza(request.getRaza());
        p.setMedida(request.getMedida());
        p.setEdadMin(request.getEdadMin());
        p.setEdadMax(request.getEdadMax());
        p.setCaracter(request.getCaracter());
        p.setDistanciaMaxKm(request.getDistanciaMaxKm());
        p.setUsuario(usuario);

        return toResponse(preferenciasRepository.save(p));
    }

    @Override
    public PreferenciasResponseDTO updatePreferencias(Long id, PreferenciasUpdateDTO update) {
        Preferencias p = preferenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preferencias no encontradas con id: " + id));

        if (update.getRaza() != null) p.setRaza(update.getRaza());
        if (update.getMedida() != null) p.setMedida(update.getMedida());
        if (update.getEdadMin() != null) p.setEdadMin(update.getEdadMin());
        if (update.getEdadMax() != null) p.setEdadMax(update.getEdadMax());
        if (update.getCaracter() != null) p.setCaracter(update.getCaracter());
        if (update.getDistanciaMaxKm() != null) p.setDistanciaMaxKm(update.getDistanciaMaxKm());

        validarRangoEdades(p.getEdadMin(), p.getEdadMax());

        return toResponse(preferenciasRepository.save(p));
    }

    @Override
    public void deletePreferencias(Long id) {
        if (!preferenciasRepository.existsById(id)) {
            throw new RuntimeException("Preferencias no encontradas con id: " + id);
        }
        preferenciasRepository.deleteById(id);
    }

    // -------- helpers --------
    private PreferenciasResponseDTO toResponse(Preferencias p) {
        PreferenciasResponseDTO dto = new PreferenciasResponseDTO();
        dto.setId(p.getId());
        dto.setRaza(p.getRaza());
        dto.setMedida(p.getMedida());
        dto.setEdadMin(p.getEdadMin());
        dto.setEdadMax(p.getEdadMax());
        dto.setCaracter(p.getCaracter());
        dto.setDistanciaMaxKm(p.getDistanciaMaxKm());
        dto.setUsuarioId(p.getUsuario() != null ? p.getUsuario().getId() : null);
        return dto;
    }

    private void validarRangoEdades(Integer min, Integer max) {
        if (min != null && max != null && min > max) {
            throw new RuntimeException("edadMin no puede ser mayor que edadMax.");
        }
    }
}

