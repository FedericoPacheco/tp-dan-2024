package isi.dan.ms.clientes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms.clientes.dao.ClienteRepository;
import isi.dan.ms.clientes.dao.UsuarioHabilitadoRepository;
import isi.dan.ms.clientes.model.Cliente;
import isi.dan.ms.clientes.model.UsuarioHabilitado;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsuarioHabilitadoService {
    
    @Autowired
    private UsuarioHabilitadoRepository usuarioHabilitadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<UsuarioHabilitado> findAll() {
        return usuarioHabilitadoRepository.findAll();
    }

    public Optional<UsuarioHabilitado> findById(Integer id) {
        return usuarioHabilitadoRepository.findById(id);
    }

    public Optional<UsuarioHabilitado> findByEmail(String email) {
        return usuarioHabilitadoRepository.findByCorreoElectronico(email);
    }
    public List<UsuarioHabilitado> findByClient(Integer idCliente) {
        return usuarioHabilitadoRepository.findUsuarioHabilitadosByClienteId(idCliente);
    }

    public UsuarioHabilitado save(UsuarioHabilitado usuarioHabilitado) {
        return usuarioHabilitadoRepository.save(usuarioHabilitado);
    }

    public UsuarioHabilitado update(UsuarioHabilitado usuarioHabilitado) {
        return usuarioHabilitadoRepository.save(usuarioHabilitado);
    }

    public void deleteById(Integer id) {
        usuarioHabilitadoRepository.deleteById(id);
    }

    public Cliente asignarCliente(Integer idCliente, Integer idUsuarioHabilitado) throws NoSuchElementException {
        UsuarioHabilitado usuarioHabilitado = usuarioHabilitadoRepository.findById(idUsuarioHabilitado).get();
        Cliente cliente = clienteRepository.findById(idCliente).get();
        
        usuarioHabilitado.setCliente(cliente);
        cliente.getUsuariosHabilitados().add(usuarioHabilitado);

        usuarioHabilitadoRepository.save(usuarioHabilitado);
        clienteRepository.save(cliente);
    
        return cliente;
    }
}
