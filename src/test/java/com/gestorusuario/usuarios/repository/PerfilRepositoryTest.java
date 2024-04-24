package com.gestorusuario.usuarios.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gestorusuario.usuarios.model.Perfil;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PerfilRepositoryTest {
    
    @Autowired
    private PerfilRepository perfilRepository;

    @Test
    public void guardarPerfilTest(){
        Perfil perf = new Perfil();
        perf.setNombreperfil("Tesorero");            

        Perfil resulPerfil = perfilRepository.save(perf);

        assertNotNull(resulPerfil.getIdperfil());
        assertEquals("Tesorero", resulPerfil.getNombreperfil());
    }

    @Test
    public void eliminarPerfilTest() {
        
        Perfil perfil = new Perfil();
        perfil.setNombreperfil("Tesorero");
        perfilRepository.save(perfil); 

        Long idPerfil = perfil.getIdperfil();

        perfilRepository.deleteById(idPerfil);
        Optional<Perfil> perfilEliminado = perfilRepository.findById(idPerfil);
        assertFalse(perfilEliminado.isPresent()); 
    }
   
}
