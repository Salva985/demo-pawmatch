package com.example.demo.dto.mascota;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import com.example.demo.enums.Sexo;
import lombok.Data;

@Data
public class MascotaResponseDTO {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Sexo sexo;
    private Integer edad;
    private Medida medida;
    private Caracter caracter;
    private String descripcion;
    private String fotoUrl;
    private Long propietarioId;
}
