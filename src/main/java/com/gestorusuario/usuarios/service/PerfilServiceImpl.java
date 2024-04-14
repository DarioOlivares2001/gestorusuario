package com.gestorusuario.usuarios.service;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

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
    
}
