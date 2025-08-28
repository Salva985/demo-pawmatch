package com.example.demo.dto.mensaje;

import com.example.demo.enums.EstadoMensaje;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensajeRequestDTO {
    @NotNull(message = "El chat es obligatorio")
    private Long chatId;

    @NotNull(message= "El remitente es obligatorio")
    private Long remitenteId;

    @NotBlank(message = "El contenido no puede estar vacio")
    @Size(max = 500, message = "EL mensaje no puede superar los 500 caracteres")
    private String contenido;

    private EstadoMensaje estado;
}
