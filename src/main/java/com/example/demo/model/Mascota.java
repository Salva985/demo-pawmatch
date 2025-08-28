package com.example.demo.model;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import com.example.demo.enums.Sexo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 50, message = "La especie no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 50)
    private String especie;

    @Size(max = 50, message = "La raza no puede tener más de 50 caracteres")
    @Column(length = 50)
    private String raza;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull(message = "El tamaño es obligatorio")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 50, message = "La edad no puede superar los 50 años")
    @Column(nullable = false)
    private Integer edad;

    @NotNull(message = "El tamaño es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Medida medida;

    @NotNull(message = "El tamaño es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Caracter caracter;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    @Size(max = 255, message = "La URL de la foto no puede superar los 255 caracteres")
    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    /**
     * Relación N:1 con Usuario
     * Cada mascota pertenece a un único usuario (dueño)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario propietario;

    @ManyToMany(mappedBy = "mascotasParticipantes")
    private Set<Evento> eventos = new HashSet<>();
}
