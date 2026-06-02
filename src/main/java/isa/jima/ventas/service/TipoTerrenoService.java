package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.TipoTerrenoRequest;
import isa.jima.ventas.dto.response.TipoTerrenoResponse;
import isa.jima.ventas.entity.TipoTerreno;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.TipoTerrenoRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoTerrenoService {

    private final TipoTerrenoRepository tipoTerrenoRepository;

    @Transactional(readOnly = true)
    public List<TipoTerrenoResponse> listar() {
        return tipoTerrenoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoTerrenoResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public TipoTerrenoResponse crear(TipoTerrenoRequest request) {
        TipoTerreno tipoTerreno = new TipoTerreno();
        tipoTerreno.setNombre(request.nombre());
        return toResponse(tipoTerrenoRepository.save(tipoTerreno));
    }

    public TipoTerrenoResponse actualizar(Integer id, TipoTerrenoRequest request) {
        TipoTerreno tipoTerreno = buscarPorId(id);
        tipoTerreno.setNombre(request.nombre());
        return toResponse(tipoTerrenoRepository.save(tipoTerreno));
    }

    public void eliminar(Integer id) {
        if (!tipoTerrenoRepository.existsById(id)) {
            throw new ResourceNotFoundException("TipoTerreno no encontrado: " + id);
        }
        tipoTerrenoRepository.deleteById(id);
    }

    private TipoTerreno buscarPorId(Integer id) {
        return tipoTerrenoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTerreno no encontrado: " + id));
    }

    private TipoTerrenoResponse toResponse(TipoTerreno tipoTerreno) {
        return new TipoTerrenoResponse(tipoTerreno.getIdTipoTerreno(), tipoTerreno.getNombre());
    }
}
