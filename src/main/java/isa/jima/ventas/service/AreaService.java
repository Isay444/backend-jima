package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.AreaRequest;
import isa.jima.ventas.dto.response.AreaResponse;
import isa.jima.ventas.entity.Area;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.AreaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public List<AreaResponse> listar() {
        return areaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AreaResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public AreaResponse crear(AreaRequest request) {
        Area area = new Area();
        mapearRequest(area, request);
        return toResponse(areaRepository.save(area));
    }

    public AreaResponse actualizar(Integer id, AreaRequest request) {
        Area area = buscarPorId(id);
        mapearRequest(area, request);
        return toResponse(areaRepository.save(area));
    }

    public void eliminar(Integer id) {
        if (!areaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Area no encontrada: " + id);
        }
        areaRepository.deleteById(id);
    }

    private Area buscarPorId(Integer id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area no encontrada: " + id));
    }

    private void mapearRequest(Area area, AreaRequest request) {
        area.setNombre(request.nombre());
        area.setDescripcion(request.descripcion());
        area.setActivo(request.activo());
    }

    private AreaResponse toResponse(Area area) {
        return new AreaResponse(
                area.getIdArea(),
                area.getNombre(),
                area.getDescripcion(),
                area.getActivo(),
                area.getFechaRegistro());
    }
}
