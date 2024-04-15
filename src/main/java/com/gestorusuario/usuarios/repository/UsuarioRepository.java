package com.gestorusuario.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestorusuario.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
