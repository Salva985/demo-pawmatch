package com.example.demo.service.impl;

import com.example.demo.dto.mensaje.MensajeRequestDTO;
import com.example.demo.dto.mensaje.MensajeResponseDTO;
import com.example.demo.dto.mensaje.MensajeUpdateDTO;
import com.example.demo.enums.EstadoMensaje;
import com.example.demo.model.Chat;
import com.example.demo.model.Mascota;
import com.example.demo.model.Mensaje;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.MensajeRepository;
import com.example.demo.service.interfaces.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MensajeServiceImpl implements MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public List<MensajeResponseDTO> getAllMensajes() {
        return mensajeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MensajeResponseDTO> getMensajesByChat(Long chatId) {
        return mensajeRepository.findByChat_Id(chatId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MensajeResponseDTO getMensajeById(Long id) {
        Mensaje m = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));
        return toResponse(m);
    }

    @Override
    public MensajeResponseDTO createMensaje(MensajeRequestDTO request) {
        Chat chat = chatRepository.findById(request.getRemitenteId())
                .orElseThrow(() -> new RuntimeException("Chat no encontrado con id: " + request.getChatId()));

        Mascota remitente = mascotaRepository.findById(request.getRemitenteId())
                .orElseThrow(() -> new RuntimeException("Mascota remitente no encontrada con id: " + request.getRemitenteId()));

        Mensaje m = new Mensaje();
        m.setChat(chat);
        m.setRemitente(remitente);
        m.setContenido(request.getContenido());
        m.setEstado(request.getEstado() != null ? request.getEstado() : EstadoMensaje.ENVIADO);

        return toResponse(mensajeRepository.save(m));
    }

    @Override
    public MensajeResponseDTO updateMensaje(Long id, MensajeUpdateDTO update) {
        Mensaje m = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));

        if (update.getContenido() != null) m.setContenido(update.getContenido());
        if (update.getEstado() != null) m.setEstado(update.getEstado());

        return toResponse(mensajeRepository.save(m));
    }

    @Override
    public void deleteMensaje(Long id) {
        if (!mensajeRepository.existsById(id)) {
            throw new RuntimeException("Mensaje no encontrado con id: " + id);
        }
        mensajeRepository.deleteById(id);
    }

    // -------- helpers --------
    private MensajeResponseDTO toResponse(Mensaje m) {
        MensajeResponseDTO dto = new MensajeResponseDTO();
        dto.setId(m.getId());
        dto.setChatId(m.getChat() != null ? m.getChat().getId() : null);
        dto.setRemitenteId(m.getRemitente() != null ? m.getRemitente().getId() : null);
        dto.setContenido(m.getContenido());
        dto.setFechaEnvio(m.getFechaEnvio());
        dto.setEstado(m.getEstado());
        return dto;
    }
}
