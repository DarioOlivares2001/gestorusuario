package com.gestorusuario.usuarios.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.gestorusuario.usuarios.model.Perfil;
import com.gestorusuario.usuarios.model.Usuario;
import com.gestorusuario.usuarios.service.PerfilService;
import com.gestorusuario.usuarios.service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;


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
    public CollectionModel<EntityModel<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        log.info("GET /usuarios");
        log.info("Retornando todos los datos de usuarios");

        List<EntityModel<Usuario>> usuarioResources = usuarios.stream()
            .map(usuario -> EntityModel.of(usuario,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(usuario.getId())).withSelfRel()
            ))
            .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios());
        CollectionModel<EntityModel<Usuario>> resources = CollectionModel.of(usuarioResources, linkTo.withRel("usuarios"));

        return resources;
    }


    /*@GetMapping
    public List<Usuario> getAllUsuarios() {
        log.info("GET / Usuarios");
        log.info("Retornando todos los datos de Usuarios con Perfil");
        return usuarioService.getAllUsuarios();
    }*/

    @GetMapping("/{id}")
    public EntityModel<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        if (usuario.isPresent()) {
            return EntityModel.of(usuario.get(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).withRel("all-usuarios"));
        } else {
            throw new UsuarioNotFoundException("Usuario no encontrado con el ID: " + id);
        }
    }

    /* 

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> user = usuarioService.getUsuarioById(id);
        if(user.isEmpty())
        {
            log.error("No se encontró el usuario con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No existe el usuario con ID " + id));
        }

        return ResponseEntity.ok(user);
        
    }*/

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Usuario usuario)  {
        List<Usuario> listauser = usuarioService.getAllUsuarios();
        String user = "";
        String password = "";

        for(Usuario u : listauser)
        {
            if(u.getNombre().equals(usuario.getNombre())){
                user = u.getNombre();
                password = u.getPassword();
            }
        }

        
        if(user == "")
        {
            log.error("ERRRO INICIO SESSION!!!");
            log.error("No se encontró el usuario nombre {}", usuario.getNombre());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No existe el usuario con nombre " + usuario.getNombre()));
        }

        if(!password.equals(usuario.getPassword()))
        {
            log.error("ERRRO INICIO SESSION!!!");
            log.error("La password ingresada no coincide para el usuario {}", usuario.getNombre());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("La password no coincide para el usuario " + usuario.getNombre()));
        }

        log.error("USUARIO LOGUEADO");
        return ResponseEntity.ok(usuario);
        
    }

    @GetMapping("/perfiles")
    public CollectionModel<EntityModel<Perfil>> getAllPerfiles() {
        List<Perfil> perfiles = perfilService.getAllPerfiles();
        log.info("GET /usuarios/perfiles");
        log.info("Lista de perfiles que pueden ser asignados a un usuario");

        List<EntityModel<Perfil>> perfilResources = perfiles.stream()
            .map(perfil -> EntityModel.of(perfil,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPerfilById(perfil.getIdperfil())).withSelfRel()
            ))
            .collect(Collectors.toList());

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPerfiles());
        CollectionModel<EntityModel<Perfil>> resources = CollectionModel.of(perfilResources, linkTo.withRel("perfiles"));

        return resources;
    }

    @GetMapping("/perfiles/{id}")
    public EntityModel<Perfil> getPerfilById(@PathVariable Long id) {
        Optional<Perfil> perfil = perfilService.getPerfilById(id);
        if (perfil.isPresent()) {
            return EntityModel.of(perfil.get(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPerfilById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPerfiles()).withRel("all-perfiles"));
        } else {
            throw new PerfilNotFoundException("Perfil no encontrado con el ID: " + id);
        }
    }

    /*@GetMapping("/perfiles")
    public List<Perfil> getAllPerfiles() {
        log.info("GET / Perfiles");
        log.info("Lista de perfiles que pueden ser asignados a un usuario");
        return perfilService.getAllPerfiles();
    }*/

    @PostMapping("/perfiles")
    public EntityModel<Perfil> createPerfil(@RequestBody Perfil perfil) {
         Perfil nuevoPerfil = perfilService.createPerfil(perfil);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPerfilById(nuevoPerfil.getIdperfil())).withSelfRel();
        Link allPerfilesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllPerfiles()).withRel("all-perfiles");
        return EntityModel.of(nuevoPerfil, selfLink, allPerfilesLink);

    }

    /*@PostMapping("/perfiles")
    public ResponseEntity<Object> createPerfil(@RequestBody Perfil perfil) {
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
    }*/


    
    
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
    

    @PostMapping("/usuarios")
    public ResponseEntity<EntityModel<Usuario>> createUsuario(@RequestBody Usuario usuario) {
        List<Usuario> listaUsuarios = usuarioService.getAllUsuarios();
        for (Usuario u : listaUsuarios) {
            if (u.getNombre().equals(usuario.getNombre())) {
                // El usuario ya existe, se devuelve un error
                log.error("El usuario ya existe: {}", usuario.getNombre());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(EntityModel.of(null, Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).toUriComponentsBuilder().build().toUriString()).withRel("usuarios")));
            }
        }
    
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        log.info("Usuario creado");
    
        // Se agrega un enlace a la colección de usuarios en la respuesta
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(nuevoUsuario.getId())).withSelfRel();
        Link usuariosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).withRel("usuarios");
    
        // Se devuelve la respuesta con el nuevo usuario y los enlaces
        return ResponseEntity.created(selfLink.toUri())
                .body(EntityModel.of(nuevoUsuario, selfLink, usuariosLink));
    }


    
    /*@PostMapping
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
    }*/


    @PutMapping("/usuarios/{id}")
    public ResponseEntity<EntityModel<Usuario>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (usuario.getNombre().isEmpty()) {
            log.error("El nombre de usuario no puede ser vacío: {}", usuario);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(EntityModel.of(null, Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).toUriComponentsBuilder().build().toUriString()).withRel("usuarios")));
        }

        if (usuario.getPassword().isEmpty()) {
            log.error("La contraseña de usuario no puede ser vacía: {}", usuario);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(EntityModel.of(null, Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).toUriComponentsBuilder().build().toUriString()).withRel("usuarios")));
        }

        Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuario);
        log.info("Usuario actualizado");

        // Se agrega un enlace al usuario actualizado y a la colección de usuarios en la respuesta
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getUsuarioById(id)).withSelfRel();
        Link usuariosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUsuarios()).withRel("usuarios");

        // Se devuelve la respuesta con el usuario actualizado y los enlaces
        return ResponseEntity.ok(EntityModel.of(usuarioActualizado, selfLink, usuariosLink));
    }

   /* @PutMapping("/{id}")
    public ResponseEntity<Object> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) 
    {
        if(usuario.getNombre().equals(""))
        {
            log.error("El nombre de usuario no puede ser vacio {}", usuario);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("El nombre no puede ser vacio " + usuario));
        }

        if(usuario.getPassword().equals(""))
        {
            log.error("La password de usuario no puede ser vacia {}", usuario);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("La password no puede ser vacio " + usuario));
        }

        Usuario upduser = usuarioService.updateUsuario(id, usuario);
        log.info("Usuario Actualizado");
        return ResponseEntity.ok(upduser);
    }*/


       
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
