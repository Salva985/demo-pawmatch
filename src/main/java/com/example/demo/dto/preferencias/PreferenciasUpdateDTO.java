package com.example.demo.dto.preferencias;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import lombok.Data;

@Data
public class PreferenciasUpdateDTO {
    private String raza;
    private Medida medida;
    private Integer edadMax;
    private Integer edadMin;
    private Caracter caracter;
    private Integer distanciaMaxKm;
}
