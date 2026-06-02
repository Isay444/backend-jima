package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.ClienteRequest;
import isa.jima.ventas.dto.response.ClienteResponse;
import isa.jima.ventas.entity.Cliente;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.entity.Municipio;
import isa.jima.ventas.repository.ClienteRepository;
import isa.jima.ventas.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final MunicipioRepository municipioRepository;

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + id));
        return toResponse(cliente);
    }

    public ClienteResponse crear(ClienteRequest request) {
        Cliente cliente = new Cliente();
        mapearRequest(cliente, request);
        // el municipio debe setearse antes de guardar para que @PrePersist pueda leer su nombre
        if (request.idMunicipio() != null) {
            cliente.setMunicipio(buscarMunicipio(request.idMunicipio()));
        }
        return toResponse(clienteRepository.save(cliente));
    }

    public ClienteResponse actualizar(Integer id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + id));
        mapearRequest(cliente, request);
        if (request.idMunicipio() != null) {
            cliente.setMunicipio(buscarMunicipio(request.idMunicipio()));
        }
        return toResponse(clienteRepository.save(cliente));
    }

    public void eliminar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponse toResponse(Cliente cliente) {
        Municipio municipio = cliente.getMunicipio();
        return new ClienteResponse(
                cliente.getIdClientes(),
                cliente.getNombreS(),
                cliente.getApellidoPaterno(),
                cliente.getApellidoMaterno(),
                cliente.getDireccion(), // Aqui ya vendra armada automaticamente
                cliente.getLocalidad(),
                cliente.getColonia(),
                cliente.getCalleNumero(),
                cliente.getCodigoPostal(),
                municipio != null ? municipio.getIdMunicipios() : null,
                municipio != null ? municipio.getNombre() : null,
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getTipo(),
                cliente.getReferenciado());
    }

    private void mapearRequest(Cliente cliente, ClienteRequest request) {
        cliente.setNombreS(request.nombreS());
        cliente.setApellidoPaterno(request.apellidoPaterno());
        cliente.setApellidoMaterno(request.apellidoMaterno());
        // cliente.setDireccion(request.direccion());
        cliente.setLocalidad(request.localidad());
        cliente.setColonia(request.colonia());
        cliente.setCalleNumero(request.calleNumero());
        cliente.setCodigoPostal(request.codigoPostal());
        cliente.setTelefono(request.telefono());
        cliente.setEmail(request.email());
        cliente.setTipo(request.tipo());
        cliente.setReferenciado(request.referenciado());
    }

    private Municipio buscarMunicipio(Integer idMunicipio) {
        return municipioRepository.findById(idMunicipio)
                .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado: " + idMunicipio));
    }
}
