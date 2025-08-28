package com.example.demo.controller;

import com.example.demo.dto.match.MatchRequestDTO;
import com.example.demo.dto.match.MatchResponseDTO;
import com.example.demo.dto.match.MatchUpdateDTO;
import com.example.demo.service.interfaces.MatchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    public MatchController(MatchService matchService) { this.matchService = matchService; }

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> getAll() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @PostMapping
    public ResponseEntity<MatchResponseDTO> create(@Valid @RequestBody MatchRequestDTO body) {
        MatchResponseDTO created = matchService.createMatch(body);
        return ResponseEntity.created(URI.create("/api/matches/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody MatchUpdateDTO body) {
        return ResponseEntity.ok(matchService.updateMatch(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}