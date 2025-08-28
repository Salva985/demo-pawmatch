package com.example.demo.dto.match;

import com.example.demo.enums.EstadoMatch;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchRequestDTO {
    @NotNull
    private Long mascotaOrigenId;

    @NotNull
    private Long mascotaDestinoId;

    private EstadoMatch estado;
}
