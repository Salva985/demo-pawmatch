package com.example.demo.dto.evento;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventoRequestDTO {
    @NotBlank @Size(max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaEvento;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 150)
    private String ubicacion;

    private List<Long> mascotasIds;
}
