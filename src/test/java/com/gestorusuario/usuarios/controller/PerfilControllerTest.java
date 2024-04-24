package com.gestorusuario.usuarios.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gestorusuario.usuarios.model.Perfil;
import com.gestorusuario.usuarios.model.Usuario;
import com.gestorusuario.usuarios.service.PerfilService;
import com.gestorusuario.usuarios.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class PerfilControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private PerfilService perfilService;

    @Test
    public void getAllUsuariosTest() throws Exception {
        // Datos de prueba
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("MONSE");

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("LEONIDAS");

        Usuario usuario3 = new Usuario();
        usuario3.setNombre("VICTOR");

        List<Usuario> usuarios = List.of(usuario1, usuario2, usuario3);

        // Simular el servicio para devolver los usuarios
        when(usuarioService.getAllUsuarios()).thenReturn(usuarios);

        // Realizar la solicitud GET y verificar la respuesta
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList[0].nombre").value("MONSE"))
                .andExpect(jsonPath("$._embedded.usuarioList[1].nombre").value("LEONIDAS"))
                .andExpect(jsonPath("$._embedded.usuarioList[2].nombre").value("VICTOR"));
    }

    @Test
    public void getAllPerfilesTest() throws Exception {
        // Datos de prueba
        Perfil perfil1 = new Perfil();
        perfil1.setNombreperfil("BODEGUERO");

        Perfil perfil2 = new Perfil();
        perfil2.setNombreperfil("COMEX");

        Perfil perfil3 = new Perfil();
        perfil3.setNombreperfil("ABASTECIMIENTO");

        List<Perfil> perfiles = List.of(perfil1, perfil2, perfil3);

        // Simular el servicio para devolver los perfiles
        when(perfilService.getAllPerfiles()).thenReturn(perfiles);

        // Realizar la solicitud GET y verificar la respuesta
        mockMvc.perform(get("/usuarios/perfiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.perfilList[0].nombreperfil").value("BODEGUERO"))
                .andExpect(jsonPath("$._embedded.perfilList[1].nombreperfil").value("COMEX"))
                .andExpect(jsonPath("$._embedded.perfilList[2].nombreperfil").value("ABASTECIMIENTO"));
    }
}