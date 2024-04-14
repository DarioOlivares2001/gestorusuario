package com.gestorusuario.usuarios.service;

import java.util.List;
import java.util.Optional;
import com.gestorusuario.usuarios.model.Perfil;

public interface PerfilService {

    List<Perfil> getAllPerfiles();
    Optional<Perfil> getPerfilById(Long idperfil);
    Perfil createPerfil(Perfil perfil);
    void deletePerfil(Long id);


        
}
