package com.example.demo.service.interfaces;

import com.example.demo.dto.mensaje.MensajeRequestDTO;
import com.example.demo.dto.mensaje.MensajeResponseDTO;
import com.example.demo.dto.mensaje.MensajeUpdateDTO;

import java.util.List;

public interface MensajeService {
    List<MensajeResponseDTO> getAllMensajes();
    List<MensajeResponseDTO> getMensajesByChat(Long chatId);

    MensajeResponseDTO getMensajeById(Long id);

    MensajeResponseDTO createMensaje(MensajeRequestDTO request);

    MensajeResponseDTO updateMensaje(Long id, MensajeUpdateDTO update);

    void deleteMensaje(Long id);
}
