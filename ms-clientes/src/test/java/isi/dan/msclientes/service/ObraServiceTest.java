package isi.dan.msclientes.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.dao.UsuarioHabilitadoRepository;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.model.UsuarioHabilitado;

// https://stackoverflow.com/questions/49441049/junit-5-does-not-execute-method-annotated-with-beforeeach


@SpringBootTest
public class ObraServiceTest {
    
    Obra obra;
    Cliente cliente;
    UsuarioHabilitado usuario;

    @MockBean
    ObraRepository obraRepository;

    @MockBean
    ClienteRepository clienteRepository;

    @MockBean
    UsuarioHabilitadoRepository usuarioHabilitadoRepository;

    @Autowired
    ObraService obraService;

    @BeforeAll 
    public static void beforeAll() {

    }

    @BeforeEach
    public void beforeEach() {
        usuario = new UsuarioHabilitado();
        usuario.setId(1);

        cliente = new Cliente();
        cliente.setId(1);

        obra = new Obra();
        obra.setId(1);

        Mockito.when(obraRepository.findById(obra.getId())).thenReturn(Optional.of(obra));
        Mockito.when(clienteRepository.findById(obra.getId())).thenReturn(Optional.of(cliente));
        Mockito.when(usuarioHabilitadoRepository.findById(obra.getId())).thenReturn(Optional.of(usuario));

        Mockito.when(obraRepository.save(obra)).thenReturn(obra);
        Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);
        Mockito.when(usuarioHabilitadoRepository.save(usuario)).thenReturn(usuario);
    }


    @Test 
    public void asignarClienteUsuarioNoHabilitado() {
        assertThrows(Exception.class, () -> obraService.asignarCliente(usuario.getId(), cliente.getId(), obra.getId()));
    }

    @Test 
    public void asignarClienteUsuarioHabilitadoObraNoFinalizada() {
        cliente.getUsuariosHabilitados().add(usuario);
        usuario.setCliente(cliente);
        obra.setEstado(EstadoObra.PENDIENTE); // No debe hacerse asi en produccion; usar cambiarEstado()
        
        // No se tira una excepcion y se guarda una unica vez en el repo
        assertDoesNotThrow(() -> obraService.asignarCliente(usuario.getId(), cliente.getId(), obra.getId()));
        verify(obraRepository, times(1)).save(obra);
    }

    /*
    @Test 
    public void asignarClienteUsuarioHabilitadoObraFinalizada() {
        cliente.getUsuariosHabilitados().add(usuario);
        obra.setEstado(EstadoObra.FINALIZADA); // No debe hacerse asi en produccion; usar cambiarEstado()
        
        assertThrows(IllegalStateException.class, () -> obraService.asignarCliente(usuario.getId(), cliente.getId(), obra.getId()));
    }
    */

    // -----------------------------------------------------------------------------------------------------------------------------------------

    @Test 
    public void cambiarEstadoUsuarioNoHabilitado() {
        assertThrows(Exception.class, () -> obraService.cambiarEstado(usuario.getId(), obra.getId(), null));
        verify(obraRepository, times(0)).save(obra);
    }

    @Test
    public void cambiarEstadoHabilitadaAPendiente() throws NoSuchElementException, IllegalStateException, Exception {
        cliente.getUsuariosHabilitados().add(usuario);
        usuario.setCliente(cliente);
        obra.setEstado(EstadoObra.HABILITADA);
        obra.setCliente(cliente);

        assertDoesNotThrow(() -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.PENDIENTE));
        verify(obraRepository, times(1)).save(obra);
    }

    @Test
    public void cambiarEstadoHabilitadaAFinalizada() throws NoSuchElementException, IllegalStateException, Exception {
        cliente.getUsuariosHabilitados().add(usuario);
        usuario.setCliente(cliente);
        obra.setEstado(EstadoObra.HABILITADA);
        obra.setCliente(cliente);
        
        List<Obra> obrasPend = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            obrasPend.add(new Obra());
            obrasPend.get(i).setId(i + 2);
            obrasPend.get(i).setEstado(EstadoObra.PENDIENTE);
        }
        Mockito.when(obraRepository.findByEstado(EstadoObra.PENDIENTE)).thenReturn(obrasPend);

        assertDoesNotThrow(() -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.FINALIZADA));
        verify(obraRepository, times(1)).save(obra);
        assertTrue(obrasPend.get(0).getEstado().equals(EstadoObra.HABILITADA));
        verify(obraRepository, times(1)).save(obrasPend.get(0));
    }

    @Test
    public void cambiarEstadoPendienteAHabilitada() throws NoSuchElementException, IllegalStateException, Exception {
        cliente.getUsuariosHabilitados().add(usuario);
        usuario.setCliente(cliente);
        obra.setEstado(EstadoObra.PENDIENTE);
        obra.setPresupuesto(BigDecimal.valueOf(100));
        obra.setCliente(cliente);

        // Maxima cantidad de obras en ejecucion
        cliente.setMaximaCantidadObrasEnEjecucion(0);
        assertThrows(Exception.class, () -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.HABILITADA));
        verify(obraRepository, times(0)).save(obra);

        // Maximo descubierto
        cliente.setMaximaCantidadObrasEnEjecucion(Integer.MAX_VALUE);
        cliente.setMaximoDescubierto(BigDecimal.valueOf(-1));
        assertThrows(Exception.class, () -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.HABILITADA));
        verify(obraRepository, times(0)).save(obra);

        // Todo en regla
        cliente.setMaximaCantidadObrasEnEjecucion(Integer.MAX_VALUE);
        cliente.setMaximoDescubierto(BigDecimal.valueOf(Integer.MAX_VALUE));
        assertDoesNotThrow(() -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.HABILITADA));
        verify(obraRepository, times(1)).save(obra);
    }

    @Test
    public void cambiarEstadoPendienteAFinalizada() {
        cliente.getUsuariosHabilitados().add(usuario);
        usuario.setCliente(cliente);
        obra.setEstado(EstadoObra.PENDIENTE);
        obra.setCliente(cliente);
        
        assertThrows(Exception.class, () -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.FINALIZADA));
        verify(obraRepository, times(0)).save(obra);
    }

    @Test
    public void cambiarEstadoTriviales() {
        cliente.getUsuariosHabilitados().add(usuario);
        cliente.setMaximaCantidadObrasEnEjecucion(Integer.MAX_VALUE);
        cliente.setMaximoDescubierto(BigDecimal.valueOf(Integer.MAX_VALUE));
        usuario.setCliente(cliente);
        obra.setCliente(cliente);
        obra.setPresupuesto(BigDecimal.valueOf(100));

        obra.setEstado(EstadoObra.PENDIENTE);
        assertDoesNotThrow(() -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.PENDIENTE));
        
        obra.setEstado(EstadoObra.HABILITADA);
        assertDoesNotThrow(() -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.HABILITADA));

        obra.setEstado(EstadoObra.FINALIZADA);
        assertDoesNotThrow(() -> obraService.cambiarEstado(usuario.getId(), obra.getId(), EstadoObra.FINALIZADA));
    }
}
