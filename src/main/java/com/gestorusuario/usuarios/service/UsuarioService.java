package com.gestorusuario.usuarios.service;

import java.util.List;
import java.util.Optional;

import com.gestorusuario.usuarios.model.Usuario;

public interface UsuarioService {
    List<Usuario> getAllUsuarios();
    Optional<Usuario> getUsuarioById(Long id);
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Long id, Usuario usuario);
    void deleteUsuario(Long id);

}
