package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    private String ciudad;

    @NotBlank
    @Email(message = " El email no tiene un formato valido")
    @Size(max = 150, message = "El email no puede tener mas de 150 caracteres")
    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @NotBlank
    @Size(max = 150, message = "El email no puede tener mas de 150 caracteres")
    @Column(nullable = false, length = 150, unique = true)
    private  String password;

    @Size(max = 150, message = "El ubicacion no puede tener mas de 150 caracteres")
    @Column(nullable = false, length = 150, unique = true)
    private String ubicacion;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    private String telefono;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascota = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Preferencias preferencias;
}
