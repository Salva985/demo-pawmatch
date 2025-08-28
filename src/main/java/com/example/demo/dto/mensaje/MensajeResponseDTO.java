package com.example.demo.dto.mensaje;

import com.example.demo.enums.EstadoMensaje;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MensajeResponseDTO {
    private Long id;
    private Long chatId;
    private Long remitenteId;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private EstadoMensaje estado;
}
