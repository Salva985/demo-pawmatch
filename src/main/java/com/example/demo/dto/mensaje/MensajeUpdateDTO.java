package com.example.demo.dto.mensaje;

import com.example.demo.enums.EstadoMensaje;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensajeUpdateDTO {
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String contenido;

    private EstadoMensaje estado;
}
