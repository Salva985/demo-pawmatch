package com.example.demo.service.impl;

import com.example.demo.dto.usuario.UsuarioRequestDTO;
import com.example.demo.dto.usuario.UsuarioResponseDTO;
import com.example.demo.dto.usuario.UsuarioUpdateDTO;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return mapToResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioRequest) {
        Usuario u = new Usuario();
        u.setNombre(usuarioRequest.getNombre());
        u.setCiudad(usuarioRequest.getCiudad());
        u.setEmail(usuarioRequest.getEmail());
        u.setPassword(usuarioRequest.getPassword());
        u.setUbicacion(usuarioRequest.getUbicacion());
        u.setTelefono(usuarioRequest.getTelefono());

        u = usuarioRepository.save(u);
        return mapToResponseDTO(u);
    }

    @Override
    public UsuarioResponseDTO updateUsuario(Long id, UsuarioUpdateDTO usuarioUpdate) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id: " + id));

        if (usuarioUpdate.getNombre() != null)      u.setNombre(usuarioUpdate.getNombre());
        if (usuarioUpdate.getCiudad() != null)      u.setCiudad(usuarioUpdate.getCiudad());
        if (usuarioUpdate.getEmail() != null)       u.setEmail(usuarioUpdate.getEmail());
        if (usuarioUpdate.getPassword() != null)    u.setPassword(usuarioUpdate.getPassword());
        if (usuarioUpdate.getUbicacion() != null)   u.setUbicacion(usuarioUpdate.getUbicacion());
        if (usuarioUpdate.getTelefono() != null)    u.setTelefono(usuarioUpdate.getTelefono());

        u = usuarioRepository.save(u);
        return mapToResponseDTO(u);
    }

    @Override
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // --------- helpers ---------
    private UsuarioResponseDTO mapToResponseDTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setCiudad(u.getCiudad());
        dto.setEmail(u.getEmail());
        dto.setUbicacion(u.getUbicacion());
        dto.setTelefono(u.getTelefono());
        return dto;
    }
}
