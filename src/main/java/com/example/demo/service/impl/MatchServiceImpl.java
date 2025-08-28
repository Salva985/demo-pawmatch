package com.example.demo.service.impl;

import com.example.demo.dto.match.MatchRequestDTO;
import com.example.demo.dto.match.MatchResponseDTO;
import com.example.demo.dto.match.MatchUpdateDTO;
import com.example.demo.enums.EstadoMatch;
import com.example.demo.model.Mascota;
import com.example.demo.model.Match;
import com.example.demo.repository.MascotaRepository;
import com.example.demo.repository.MatchRepository;
import com.example.demo.service.interfaces.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public List<MatchResponseDTO> getAllMatches() {
        return matchRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MatchResponseDTO getMatchById(Long id) {
        Match m = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match no encontrado con id: " + id));
        return toResponse(m);
    }

    @Override
    public MatchResponseDTO createMatch(MatchRequestDTO request) {
        Mascota origen = mascotaRepository.findById(request.getMascotaOrigenId())
                .orElseThrow(() -> new RuntimeException("Mascota origen no encontrada con id: " + request.getMascotaOrigenId()));
        Mascota destino = mascotaRepository.findById(request.getMascotaDestinoId())
                .orElseThrow(() -> new RuntimeException("Mascota destino no encontrada con id: " + request.getMascotaDestinoId()));

        Match m = new Match();
        m.setMascotaOrigen(origen);
        m.setMascotaDestino(destino);
        m.setEstado(request.getEstado() != null ? request.getEstado() : EstadoMatch.PENDIENTE);

        return toResponse(matchRepository.save(m));
    }

    @Override
    public MatchResponseDTO updateMatch(Long id, MatchUpdateDTO update) {
        Match m = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match no encontrado con id: " + id));

        if (update.getEstado() != null) {
            m.setEstado(update.getEstado());
        }

        return toResponse(matchRepository.save(m));
    }

    @Override
    public void deleteMatch(long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match no encontrado con id: " + id);
        }
        matchRepository.deleteById(id);
    }

    // -------- Helpers --------
    private MatchResponseDTO toResponse(Match m) {
        MatchResponseDTO dto = new MatchResponseDTO();
        dto.setId(m.getId());
        dto.setMascotaOrigenId(m.getMascotaOrigen() != null ? m.getMascotaOrigen().getId() : null);
        dto.setMascotaDestinoId(m.getMascotaDestino() != null ? m.getMascotaDestino().getId() : null);
        dto.setEstado(m.getEstado());
        dto.setFechaMatch(m.getFechaMatch());
        return dto;
    }
}
