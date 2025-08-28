package com.example.demo.model;

import com.example.demo.enums.Caracter;
import com.example.demo.enums.Medida;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "preferencias")
public class Preferencias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "La raza no puede tener mas de 50 caracteres")
    @Column(length = 50)
    private String raza;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Medida medida;

    @Min(value = 0, message = "La edad minima no puede ser negativa")
    @Max(value = 50, message = "La edad minima no puede superar 50")
    @Column(name = "edad_min")
    private Integer edadMin;

    @Min(value = 0, message = "La edad maxima no puede ser negativa")
    @Max(value = 50, message = "La edad maxima no puede superar 50")
    @Column(name = "edad_max")
    private Integer edadMax;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Caracter caracter;

    @Min(value = 0, message = "La distancia minima es de 1 km")
    @Max(value = 100, message = "La distancia maxima permetida es de 100 km")
    @Column(name = "distancia_max_km")
    private Integer distanciaMaxKm;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private  Usuario usuario;
}
