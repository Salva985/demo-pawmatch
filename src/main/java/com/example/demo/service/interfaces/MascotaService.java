package com.example.demo.service.interfaces;

import com.example.demo.dto.mascota.MascotaRequestDTO;
import com.example.demo.dto.mascota.MascotaResponseDTO;
import com.example.demo.dto.mascota.MascotaUpdateDTO;

import java.util.List;

public interface MascotaService {
    List<MascotaResponseDTO> getAllMascotas();

    MascotaResponseDTO getMascotaById(Long id);

    MascotaResponseDTO createMascota(MascotaRequestDTO request);

    MascotaResponseDTO updateMascota(Long id, MascotaUpdateDTO update);

    void deleteMascota(Long id);
}
