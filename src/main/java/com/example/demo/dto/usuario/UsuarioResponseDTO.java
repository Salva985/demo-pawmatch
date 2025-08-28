package com.example.demo.dto.usuario;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String ciudad;
    private String ubicacion;
    private String telefono;
}