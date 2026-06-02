package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.IngenieroRequest;
import isa.jima.ventas.dto.response.IngenieroResponse;
import isa.jima.ventas.entity.Ingeniero;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.IngenieroRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class IngenieroService {

    private final IngenieroRepository ingenieroRepository;

    @Transactional(readOnly = true)
    public List<IngenieroResponse> listar() {
        return ingenieroRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public IngenieroResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public IngenieroResponse crear(IngenieroRequest request) {
        Ingeniero ingeniero = new Ingeniero();
        mapearRequest(ingeniero, request);
        return toResponse(ingenieroRepository.save(ingeniero));
    }

    public IngenieroResponse actualizar(Integer id, IngenieroRequest request) {
        Ingeniero ingeniero = buscarPorId(id);
        mapearRequest(ingeniero, request);
        return toResponse(ingenieroRepository.save(ingeniero));
    }

    public void eliminar(Integer id) {
        if (!ingenieroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ingeniero no encontrado: " + id);
        }
        ingenieroRepository.deleteById(id);
    }

    private Ingeniero buscarPorId(Integer id) {
        return ingenieroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingeniero no encontrado: " + id));
    }

    private void mapearRequest(Ingeniero ingeniero, IngenieroRequest request) {
        ingeniero.setNombre(request.nombre());
        ingeniero.setEspecialidad(request.especialidad());
        ingeniero.setTelefono(request.telefono());
    }

    private IngenieroResponse toResponse(Ingeniero ingeniero) {
        return new IngenieroResponse(
                ingeniero.getIdIngeniero(),
                ingeniero.getNombre(),
                ingeniero.getEspecialidad(),
                ingeniero.getTelefono());
    }
}
