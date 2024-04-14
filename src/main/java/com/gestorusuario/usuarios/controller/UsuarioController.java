package com.gestorusuario.usuarios.controller;

import org.springframework.web.bind.annotation.RestController;

import com.gestorusuario.usuarios.model.Perfil;
import com.gestorusuario.usuarios.model.Usuario;
import com.gestorusuario.usuarios.service.PerfilService;
import com.gestorusuario.usuarios.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);


    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilService perfilService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        log.info("GET / Usuarios");
        log.info("Retornando todos los datos de Usuarios con Perfil");
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> user = usuarioService.getUsuarioById(id);
        if(user.isEmpty())
        {
            log.error("No se encontró el usuario con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No existe el usuario con ID " + id));
        }

        return ResponseEntity.ok(user);
        
    }

    @GetMapping("/perfiles")
    public List<Perfil> getAllPerfiles() {
        log.info("GET / Perfiles");
        log.info("Lista de perfiles que pueden ser asignados a un usuario");
        return perfilService.getAllPerfiles();
    }

    @PostMapping("/perfiles")
    public ResponseEntity<Object> postMethodName(@RequestBody Perfil perfil) {
        List<Perfil> listaPerfiles = perfilService.getAllPerfiles();
        for (Perfil p : listaPerfiles) {
            if (p.getNombreperfil().equals(perfil.getNombreperfil())) {
                // No se puede crear el perfil porque ya existe
                log.error("Ya existe un perfil con el nombre {}", perfil.getNombreperfil());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Ya existe un perfil con el nombre " + perfil.getNombreperfil()));
            }
        }

        Perfil nuevoPerfil = perfilService.createPerfil(perfil);
        log.info("Se ha creado el nuevo perfil");
        return ResponseEntity.ok(nuevoPerfil);
    }

    
    @DeleteMapping("/perfiles/{id}")
    public ResponseEntity<Object> deletePerfil(@PathVariable Long id)
    {
        List<Usuario> listausuario = usuarioService.getAllUsuarios();
        for(Usuario u : listausuario)
        {
            Perfil p = u.getPerfil();
            if(p.getIdperfil() == id)
            {
                // No se puede crear el perfil porque ya existe
                log.error("No se puede eliminar el perfil ya que esta siendo usado {}", p.getNombreperfil());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El perfil esta asignado a usuarios " + p.getNombreperfil()));
            }
        }
        perfilService.deletePerfil(id);
        log.info("Perfil eliminado");
        return ResponseEntity.ok("Perfil eliminado");
    }
    
    
    @PostMapping
    public ResponseEntity<Object> createUsuario(@RequestBody Usuario usuario) {
        List<Usuario> listausuario = usuarioService.getAllUsuarios();
        for(Usuario u : listausuario)
        {
            if(u.getNombre().equals(usuario.getNombre())){
                 // No se puede crear el perfil porque ya existe
                 log.error("El usuario ya existe {}", usuario.getNombre());
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El perfil esta asignado a usuarios " + usuario.getNombre()));
            }
        }

        Usuario newuser = usuarioService.createUsuario(usuario);
        log.info("Usuario Creado");
        return ResponseEntity.ok(newuser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) 
    {
        if(usuario.getNombre().equals(""))
        {
            log.error("El nombre de usuario no puede ser vacio {}", usuario);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El nombre no puede ser vacio " + usuario));

        }
        Usuario upduser = usuarioService.updateUsuario(id, usuario);
        log.info("Usuario Actualizado");
        return ResponseEntity.ok(upduser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id)
    {
        Optional<Usuario> u = usuarioService.getUsuarioById(id);
        if(u.isEmpty())
        {
            log.error("El usuario no existe", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El usuario que quiere eliminar no existe " + id));

        }
        log.info("Se ha eliminado el usuario");
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(u);

    }

    static class ErrorResponse {
        private final String message;
    
        public ErrorResponse(String message) {
            this.message = message;
        }
    
        public String getMessage() {
            return message;
        }
    }

}
