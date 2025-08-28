package com.example.demo.service.impl;

import com.example.demo.dto.mascota.MascotaRequestDTO;
import com.example.demo.dto.mascota.MascotaResponseDTO;
import com.example.demo.dto.mascota.MascotaUpdateDTO;
import com.example.demo.model.Mascota;
import com.example.demo.model.Usuario;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.interfaces.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MascotaServiceImpl implements MascotaService {
    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<MascotaResponseDTO> getAllMascotas() {
        return mascotaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MascotaResponseDTO getMascotaById(Long id) {
        Mascota m = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrado con id: " + id));
        return toResponse(m);
    }

    @Override
    public MascotaResponseDTO createMascota(MascotaRequestDTO request) {
        if (request.getPropietarioId() == null) {
            throw new RuntimeException("propietarioId es obligatorio");
        }
        Usuario propietario = usuarioRepository.findById(request.getPropietarioId())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario (propietario) no encontrado con id: " + request.getPropietarioId()
                ));

        Mascota m = new Mascota();
        m.setNombre(request.getNombre());
        m.setEspecie(request.getEspecie());
        m.setRaza(request.getRaza());
        m.setSexo(request.getSexo());
        m.setEdad(request.getEdad());
        m.setMedida(request.getMedida());
        m.setCaracter(request.getCaracter());
        m.setDescripcion(request.getDescripcion());
        m.setFotoUrl(request.getFotoUrl());
        m.setPropietario(propietario);

        return toResponse(mascotaRepository.save(m));
    }

    @Override
    public MascotaResponseDTO updateMascota(Long id, MascotaUpdateDTO update) {
        Mascota m = mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + id));

        if (update.getNombre() != null) m.setNombre(update.getNombre());
        if (update.getEspecie() != null) m.setEspecie(update.getEspecie());
        if (update.getRaza() != null) m.setRaza(update.getRaza());
        if (update.getSexo() != null) m.setSexo(update.getSexo());
        if (update.getEdad() != null) m.setEdad((update.getEdad()));
        if (update.getMedida() != null) m.setMedida(update.getMedida());
        if (update.getCaracter() != null) m.setCaracter(update.getCaracter());
        if (update.getDescripcion() != null) m.setDescripcion((update.getDescripcion()));
        if (update.getFotoUrl() != null) m.setFotoUrl(update.getFotoUrl());

        if (update.getPropietarioId() != null) {
            Usuario nuevo = usuarioRepository.findById(update.getPropietarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario (propietario) no encontrado con id: " + update.getPropietarioId()));
            m.setPropietario(nuevo);
        }

        return toResponse(mascotaRepository.save(m));
    }

    @Override
    public void deleteMascota(Long id) {
        if (!mascotaRepository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada con id: " + id);
        }
        mascotaRepository.deleteById(id);
    }

    private MascotaResponseDTO toResponse(Mascota m) {
        MascotaResponseDTO dto = new MascotaResponseDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setEspecie(m.getEspecie());
        dto.setRaza(m.getRaza());
        dto.setSexo(m.getSexo());
        dto.setEdad(m.getEdad());
        dto.setMedida(m.getMedida());
        dto.setCaracter(m.getCaracter());
        dto.setDescripcion(m.getDescripcion());
        dto.setFotoUrl(m.getFotoUrl());
        dto.setPropietarioId(m.getPropietario() != null ? m.getPropietario().getId() : null);
        return dto;
    }
}
