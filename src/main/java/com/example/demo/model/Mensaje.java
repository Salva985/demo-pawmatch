package com.example.demo.model;

import com.example.demo.enums.EstadoMensaje;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación N:1 con Chat
     * Un chat puede tener múltiples mensajes
     */
    @NotNull(message = "El mensaje debe pertenecer a un chat")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    /**
     * Relación N:1 con Mascota
     * Cada mensaje tiene un remitente (mascota)
     */
    @NotNull(message = "El mensaje debe tener un remitente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    private Mascota remitente;

    /**
     * Contenido del mensaje (texto)
     */
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String contenido;

    /**
     * Fecha y hora en que se envió el mensaje
     */
    @CreationTimestamp
    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    /**
     * Estado de lectura del mensaje
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    @Builder.Default
    private EstadoMensaje estado = EstadoMensaje.ENVIADO;

}