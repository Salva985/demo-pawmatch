package com.example.demo.repository;

import com.example.demo.model.Preferencias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {
    Optional<Preferencias> findByUsuario_Id(Long usuarioId);
    boolean existsByUsuario_Id(Long usuarioId);
}
