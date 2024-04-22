package com.gestorusuario.usuarios.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestorusuario.usuarios.model.Perfil;
import com.gestorusuario.usuarios.repository.PerfilRepository;

@Service
public class PerfilServiceImpl implements PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public List<Perfil> getAllPerfiles()
    {

        return perfilRepository.findAll();
    }

    public Optional<Perfil> getPerfilById(Long idperfil)
    {
        return perfilRepository.findById(idperfil);

    }
    
    public Perfil createPerfil(Perfil perfil)
    {
        return perfilRepository.save(perfil);

    }

    public void deletePerfil(Long id)
    {
        perfilRepository.deleteById(id);
    }

    public Perfil updatePerfil(Long id, Perfil perfil) {
        Optional<Perfil> optionalPerfil = perfilRepository.findById(id);
        if (optionalPerfil.isPresent()) {
            Perfil existingPerfil = optionalPerfil.get();
            existingPerfil.setNombreperfil(perfil.getNombreperfil());
            // Actualiza otros campos si es necesario
            return perfilRepository.save(existingPerfil);
        } else {
            throw new RuntimeException("No se encontró el perfil con el ID: " + id);
        }
    }
}
