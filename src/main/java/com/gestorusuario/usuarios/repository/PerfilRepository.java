package com.gestorusuario.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestorusuario.usuarios.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
}
