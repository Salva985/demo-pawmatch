package com.example.demo.dto.preferencias;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PreferenciasRequestDTO {
    @Size(max = 50)
    private String raza;

    private Medida medida;

    @Min(0)
    @Max(50)
    private Integer edadMin;

    @Min(0)
    @Max(50)
    private Integer edadMax;

    private Caracter caracter;

    @Min(0)
    @Max(100)
    private Integer distanciaMaxKm;

    private Long usuarioId;
}
