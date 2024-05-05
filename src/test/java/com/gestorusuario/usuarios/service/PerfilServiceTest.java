package com.gestorusuario.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestorusuario.usuarios.model.Perfil;
import com.gestorusuario.usuarios.repository.PerfilRepository;


@ExtendWith(MockitoExtension.class)
public class PerfilServiceTest {
    
    @InjectMocks
    private PerfilServiceImpl perfilService;

    @Mock
    private PerfilRepository perfilrepositorymock;

    @Test
    public void guardarPerfilTest(){

        Perfil perfil = new Perfil();
        perfil.setNombreperfil("Bodeguero");

        when(perfilrepositorymock.save(any())).thenReturn(perfil);

        Perfil result = perfilService.createPerfil(perfil);

        assertEquals("Bodeguero", result.getNombreperfil());

    }
   
    
    @Test
    public void eliminarPerfilTest() {
        Perfil perfilAEliminar = new Perfil();
        perfilAEliminar.setIdperfil(1L); 
        perfilAEliminar.setNombreperfil("Bodeguero");

        perfilService.deletePerfil(perfilAEliminar.getIdperfil());

        verify(perfilrepositorymock, times(1)).deleteById(perfilAEliminar.getIdperfil());
    }


    @Test
    public void buscarPerfilPorIdExistenteTest(){
        Perfil perfil = new Perfil();
        perfil.setIdperfil(1L);
        perfil.setNombreperfil("Administrador");

        when(perfilrepositorymock.findById(1L)).thenReturn(Optional.of(perfil));

        Optional<Perfil> perfilOptional = perfilService.getPerfilById(1L);
        assertTrue(perfilOptional.isPresent());
        assertEquals("Administrador", perfilOptional.get().getNombreperfil());
    }

    @Test
    public void buscarPerfilPorIdInexistenteTest(){
        when(perfilrepositorymock.findById(2L)).thenReturn(Optional.empty());

        Optional<Perfil> perfilOptional = perfilService.getPerfilById(2L);
        assertFalse(perfilOptional.isPresent());
    }

}
