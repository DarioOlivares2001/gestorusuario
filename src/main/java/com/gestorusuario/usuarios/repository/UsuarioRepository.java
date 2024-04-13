package com.gestorusuario.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestorusuario.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
