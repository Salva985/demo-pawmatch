package com.example.demo.dto.mascota;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import com.example.demo.enums.Sexo;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MascotaRequestDTO {
    @NotBlank
    @Size(max = 50)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    private String especie;

    @Size(max = 50)
    private String raza;

    private Sexo sexo;

    @NotNull
    @Min(0)
    @Max(50)
    private Integer edad;

    @NotNull
    private Medida medida;

    @NotNull
    private Caracter caracter;

    @Size(max = 255)
    private String descripcion;

    @Size(max = 255)
    private String fotoUrl;

    @NotNull(message = "propietarioId es obligatorio")
    private Long propietarioId;
}
