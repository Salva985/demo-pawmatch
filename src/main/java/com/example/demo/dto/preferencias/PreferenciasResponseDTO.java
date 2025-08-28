package com.example.demo.dto.preferencias;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import lombok.Data;

@Data
public class PreferenciasResponseDTO {
    private Long id;
    private String raza;
    private Medida medida;
    private Integer edadMin;
    private Integer edadMax;
    private Caracter caracter;
    private Integer distanciaMaxKm;
    private Long usuarioId;
}
