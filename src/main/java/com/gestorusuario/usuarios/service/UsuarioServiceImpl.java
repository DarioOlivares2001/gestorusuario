package com.gestorusuario.usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestorusuario.usuarios.model.Usuario;
import com.gestorusuario.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios(){
        return usuarioRepository.findAll();
    }


    @Override
    public Optional<Usuario> getUsuarioById(Long id){
        return usuarioRepository.findById(id);
    }


    @Override
    public Usuario createUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);

    }

    @Override
    public Usuario updateUsuario(Long id, Usuario usuario){
        if(usuarioRepository.existsById(id))
        {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        else
        {
            return null;

        }
    }


    @Override
    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }



    
}
