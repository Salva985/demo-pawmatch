package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario1_id", nullable = false)
    private Usuario usuario1;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario2_id", nullable = false)
    private Usuario usuario2;

    /**
     * Relación 1:1 con Match
     * Cada Chat pertenece a un único Match confirmado
     */
    @NotNull(message = "El chat debe estar asociado a un match")
    @OneToOne
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensaje> mensajes = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void normalizarPar(){
        if(usuario1 == null || usuario2 == null) return;
        Long id1 = usuario1.getId(), id2 = usuario2.getId();
        if(id1 == null || id2 == null)
            throw new IllegalStateException("Usuarios gestionados deben tener un ID");
        if(id1 > id2) { var t = usuario1; usuario1 = usuario2; usuario2 = t; }
    }
}