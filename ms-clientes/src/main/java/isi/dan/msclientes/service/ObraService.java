package isi.dan.msclientes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.dao.UsuarioHabilitadoRepository;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;
import isi.dan.msclientes.model.UsuarioHabilitado;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ObraService {
    
    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired 
    private UsuarioHabilitadoRepository usuarioHabilitadoRepository;

    public List<Obra> findAll() {
        return obraRepository.findAll();
    }

    public Optional<Obra> findById(Integer id) {
        return obraRepository.findById(id);
    }

    public Obra save(Obra obra) {
        return obraRepository.save(obra);
    }

    public Obra update(Obra obra) {
        return obraRepository.save(obra);
    }

    public void deleteById(Integer id) {
        obraRepository.deleteById(id);
    }

    /* 
     * Enunciado: "Cuando se asinga una obra a un cliente se debe verificar el estado de habilitación." 
     * Problema: no se puede verificar que la obra esté HABILITADA porque los criterios para que esté
     * en dicho estado dependen de la cantidad máxima de obras en ejecución y el máximo descubierto del cliente
     * que se está pretendiendo asignar. 
     * Solución: solo se verifica que el usuario este habilitado para el cliente. También se inicializa la obra a PENDIENTE,
     * puesto que desde ese estado puede transicionar a HABILITADA con cambiarEstado() verificando los criterios comentados
     */
    public Cliente asignarCliente(Integer idUsuario, Integer idCliente, Integer idObra) throws NoSuchElementException, IllegalStateException, Exception {
        
        Cliente cliente = clienteRepository.findById(idCliente).get();
        UsuarioHabilitado usuario = usuarioHabilitadoRepository.findById(idUsuario).get();
        Obra obra = obraRepository.findById(idObra).get();

        if (cliente.getUsuariosHabilitados().contains(usuario)) {
            //if (obra.getEstado().equals(EstadoObra.HABILITADA)) {
                obra.setCliente(cliente);
                obra.setEstado(EstadoObra.PENDIENTE); 
                cliente.getObrasAsignadas().add(obra);
                
                obraRepository.save(obra);
                clienteRepository.save(cliente); // Tecnicamente no es necesario

                return cliente;
            //} else throw new IllegalStateException("La obra con id: " + obra.getId() + " debe estar habilitada para asignarle un cliente");
        }
        else throw new Exception("El usuario con id: " + idUsuario + " no puede realizar operaciones sobre el cliente con id: " + idCliente);
    }

    /* 
     * Primero se verifica que un usuario habilitado para el cliente de la obra realice el cambio de estado.
     * Luego, se considera que las transiciones de estado válidas son:
     * HABILITADA -> PENDIENTE: no hacer nada
     * HABILITADA -> FINALIZADA: se busca la obra pendiente con id más bajo y se pone HABILITADA
     * PENDIENTE -> HABILITADA: se verifica que no se exceda la cantidad máxima de obras en ejecución y el máximo descubierto
     * HABILITADA -> HABILITADA: trivial
     * PENDIENTE -> PENDIENTE: trivial
     * FINALIZADA -> FINALIZADA: trivial
     * Si el comportamiento fuera más complejo podría considerarse usar el patrón de diseño state.
     */
    public Obra cambiarEstado(Integer idUsuario, Integer idObra, EstadoObra nuevoEstado) throws NoSuchElementException, Exception {

        UsuarioHabilitado usuario = usuarioHabilitadoRepository.findById(idUsuario).get();
        Obra obra = obraRepository.findById(idObra).get();
        Cliente cliente = obra.getCliente();

        if (usuario.getCliente().equals(cliente)) {
            if (nuevoEstado != null) {
                if (obra.getEstado().equals(EstadoObra.PENDIENTE)) {
                    if (cliente.getObrasAsignadas().size() >= cliente.getMaximaCantidadObrasEnEjecucion())
                        throw new Exception("El cliente con id:" + cliente.getId() + "llego a limite de obras asignadas");
                    else if(cliente.getDescubierto().compareTo(cliente.getMaximoDescubierto()) >= 0)
                        throw new Exception("El cliente con id:" + cliente.getId() + " llegó al máximo descubierto");
                    else if(nuevoEstado.equals(EstadoObra.FINALIZADA))
                        throw new Exception("La obra con id " + idObra + " no puede pasarse de PENDIENTE a FINALIZADA");
                    // else: nuevoEstado es PENDIENTE o HABILITADA
                } 
                else if (obra.getEstado().equals(EstadoObra.HABILITADA)) {
                    if (nuevoEstado.equals(EstadoObra.FINALIZADA)) {
                        List<Obra> obrasPendientes = obraRepository.findByEstado(EstadoObra.PENDIENTE);
                        if (obrasPendientes.size() > 0) {
                            Optional<Obra> obraPendiente = obrasPendientes.stream().min((o1, o2) -> o1.getId().compareTo(o2.getId()));
                            obraPendiente.get().setEstado(EstadoObra.HABILITADA);
                            obraRepository.save(obraPendiente.get());
                        }
                    } // else: nuevoEstado es PENDIENTE O HABILITADA
                }
                
                obra.setEstado(nuevoEstado);
                obraRepository.save(obra);
            }
            else throw new Exception("El nuevo estado no puede ser null");
            
            return obra;
        }
        else throw new Exception("El usuario con id: " + idUsuario + " no tiene permisos para cambiar el estado de la obra");
    }
}

