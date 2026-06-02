package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.ZonaEjidalRequest;
import isa.jima.ventas.dto.response.ZonaEjidalResponse;
import isa.jima.ventas.entity.ZonaEjidal;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.ZonaEjidalRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ZonaEjidalService {

    private final ZonaEjidalRepository zonaEjidalRepository;

    @Transactional(readOnly = true)
    public List<ZonaEjidalResponse> listar() {
        return zonaEjidalRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ZonaEjidalResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public ZonaEjidalResponse crear(ZonaEjidalRequest request) {
        ZonaEjidal zonaEjidal = new ZonaEjidal();
        zonaEjidal.setNombre(request.nombre());
        return toResponse(zonaEjidalRepository.save(zonaEjidal));
    }

    public ZonaEjidalResponse actualizar(Integer id, ZonaEjidalRequest request) {
        ZonaEjidal zonaEjidal = buscarPorId(id);
        zonaEjidal.setNombre(request.nombre());
        return toResponse(zonaEjidalRepository.save(zonaEjidal));
    }

    public void eliminar(Integer id) {
        if (!zonaEjidalRepository.existsById(id)) {
            throw new ResourceNotFoundException("ZonaEjidal no encontrada: " + id);
        }
        zonaEjidalRepository.deleteById(id);
    }

    private ZonaEjidal buscarPorId(Integer id) {
        return zonaEjidalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ZonaEjidal no encontrada: " + id));
    }

    private ZonaEjidalResponse toResponse(ZonaEjidal zonaEjidal) {
        return new ZonaEjidalResponse(zonaEjidal.getIdZonaEjidal(), zonaEjidal.getNombre());
    }
}
