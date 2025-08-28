package com.example.demo.repository;

import com.example.demo.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    public List<Mascota> findByPropietarioId(Long id);
}
