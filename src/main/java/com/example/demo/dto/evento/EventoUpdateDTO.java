package com.example.demo.dto.evento;

import jakarta.validation.constraints.Future;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventoUpdateDTO {
    private String nombre;
    private String descripcion;
    private String ubicacion;

    @Future
    private LocalDateTime fechaEvento;

    private List<Long> mascotaIds;
}
