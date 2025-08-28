package com.example.demo.dto.usuario;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    private String nombre;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private String ciudad;

    private String ubicacion;

    private String telefono;
}