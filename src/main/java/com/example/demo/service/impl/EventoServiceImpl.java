package com.example.demo.service.impl;

import com.example.demo.dto.evento.EventoRequestDTO;
import com.example.demo.dto.evento.EventoResponseDTO;
import com.example.demo.dto.evento.EventoUpdateDTO;
import com.example.demo.model.Evento;
import com.example.demo.repository.EventoRepository;
import com.example.demo.service.interfaces.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public List<EventoResponseDTO> getAllEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventoResponseDTO getEventoById(Long id) {
        Evento e = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
        return toResponse(e);
    }

    @Override
    public EventoResponseDTO createEvento(EventoRequestDTO request) {
        Evento e = new Evento();
        e.setNombre(request.getNombre());
        e.setDescripcion(request.getDescripcion());
        e.setUbicacion(request.getUbicacion());
        e.setFechaEvento(request.getFechaEvento());
        // NO tocamos mascotasParticipantes ni otros campos
        return toResponse(eventoRepository.save(e));
    }

    @Override
    public EventoResponseDTO updateEvento(Long id, EventoUpdateDTO update) {
        Evento e = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));

        if (update.getNombre() != null) e.setNombre(update.getNombre());
        if (update.getDescripcion() != null) e.setDescripcion(update.getDescripcion());
        if (update.getUbicacion() != null) e.setUbicacion(update.getUbicacion());
        if (update.getFechaEvento() != null) e.setFechaEvento(update.getFechaEvento());

        return toResponse(eventoRepository.save(e));
    }

    @Override
    public void deleteEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado con id: " + id);
        }
        eventoRepository.deleteById(id);
    }

    // -------- helpers --------
    private EventoResponseDTO toResponse(Evento e) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        dto.setUbicacion(e.getUbicacion());
        dto.setFechaEvento(e.getFechaEvento());
        return dto;
    }
}