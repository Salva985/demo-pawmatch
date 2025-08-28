package com.example.demo.service.interfaces;

import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.dto.usuario.UsuarioResponseDTO;
import com.example.demo.dto.usuario.UsuarioUpdateDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDTO> getAllUsuarios();

    UsuarioResponseDTO getUsuarioById(Long id);

    UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioRequest);

    UsuarioResponseDTO updateUsuario(Long id, UsuarioUpdateDTO usuarioUpdate);

    void deleteUsuario(Long id);
}
