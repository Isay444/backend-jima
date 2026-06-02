package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.PuestoRequest;
import isa.jima.ventas.dto.response.PuestoResponse;
import isa.jima.ventas.entity.Puesto;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.PuestoRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PuestoService {

    private final PuestoRepository puestoRepository;

    @Transactional(readOnly = true)
    public List<PuestoResponse> listar() {
        return puestoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PuestoResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public PuestoResponse crear(PuestoRequest request) {
        Puesto puesto = new Puesto();
        mapearRequest(puesto, request);
        return toResponse(puestoRepository.save(puesto));
    }

    public PuestoResponse actualizar(Integer id, PuestoRequest request) {
        Puesto puesto = buscarPorId(id);
        mapearRequest(puesto, request);
        return toResponse(puestoRepository.save(puesto));
    }

    public void eliminar(Integer id) {
        if (!puestoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Puesto no encontrado: " + id);
        }
        puestoRepository.deleteById(id);
    }

    private Puesto buscarPorId(Integer id) {
        return puestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado: " + id));
    }

    private void mapearRequest(Puesto puesto, PuestoRequest request) {
        puesto.setNombre(request.nombre());
        puesto.setDescripcion(request.descripcion());
        puesto.setActivo(request.activo());
    }

    private PuestoResponse toResponse(Puesto puesto) {
        return new PuestoResponse(
                puesto.getIdPuesto(),
                puesto.getNombre(),
                puesto.getDescripcion(),
                puesto.getActivo(),
                puesto.getFechaRegistro());
    }
}
