package com.example.demo.service.interfaces;

import com.example.demo.dto.match.MatchRequestDTO;
import com.example.demo.dto.match.MatchResponseDTO;
import com.example.demo.dto.match.MatchUpdateDTO;

import java.util.List;

public interface MatchService {
    List<MatchResponseDTO> getAllMatches();

    MatchResponseDTO getMatchById(Long id);

    MatchResponseDTO createMatch(MatchRequestDTO request);

    MatchResponseDTO updateMatch(Long id, MatchUpdateDTO update);

    void deleteMatch(long id);
}
