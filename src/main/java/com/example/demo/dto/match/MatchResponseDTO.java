package com.example.demo.dto.match;

import com.example.demo.enums.EstadoMatch;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchResponseDTO {
    private Long id;
    private Long mascotaOrigenId;
    private Long MascotaDestinoId;
    private EstadoMatch estado;
    private LocalDateTime fechaMatch;
}
