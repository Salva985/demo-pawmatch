package com.example.demo.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
    private String nombre;

    @Email(message = "Debe proporcionar un email válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private String ciudad;

    @Size(max = 150, message = "El ubicacion no puede tener mas de 150 caracteres")
    private String ubicacion;

    private String telefono;
}