package com.example.demo.service.interfaces;

import com.example.demo.dto.evento.EventoRequestDTO;
import com.example.demo.dto.evento.EventoResponseDTO;
import com.example.demo.dto.evento.EventoUpdateDTO;

import java.util.List;

public interface EventoService {
    List<EventoResponseDTO> getAllEventos();

    EventoResponseDTO getEventoById(Long id);

    EventoResponseDTO createEvento(EventoRequestDTO request);

    EventoResponseDTO updateEvento(Long id, EventoUpdateDTO update);

    void deleteEvento(Long id);

}
