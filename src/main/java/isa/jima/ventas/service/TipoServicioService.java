package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.TipoServicioRequest;
import isa.jima.ventas.dto.response.TipoServicioResponse;
import isa.jima.ventas.entity.TipoServicio;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.TipoServicioRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoServicioService {

    private final TipoServicioRepository tipoServicioRepository;

    @Transactional(readOnly = true)
    public List<TipoServicioResponse> listar() {
        return tipoServicioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoServicioResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public TipoServicioResponse crear(TipoServicioRequest request) {
        TipoServicio tipoServicio = new TipoServicio();
        tipoServicio.setNombre(request.nombre());
        return toResponse(tipoServicioRepository.save(tipoServicio));
    }

    public TipoServicioResponse actualizar(Integer id, TipoServicioRequest request) {
        TipoServicio tipoServicio = buscarPorId(id);
        tipoServicio.setNombre(request.nombre());
        return toResponse(tipoServicioRepository.save(tipoServicio));
    }

    public void eliminar(Integer id) {
        if (!tipoServicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("TipoServicio no encontrado: " + id);
        }
        tipoServicioRepository.deleteById(id);
    }

    private TipoServicio buscarPorId(Integer id) {
        return tipoServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoServicio no encontrado: " + id));
    }

    private TipoServicioResponse toResponse(TipoServicio tipoServicio) {
        return new TipoServicioResponse(tipoServicio.getIdTipoServicio(), tipoServicio.getNombre());
    }
}
