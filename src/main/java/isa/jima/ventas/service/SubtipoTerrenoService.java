package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.SubtipoTerrenoRequest;
import isa.jima.ventas.dto.response.SubtipoTerrenoResponse;
import isa.jima.ventas.entity.SubtipoTerreno;
import isa.jima.ventas.entity.TipoTerreno;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.SubtipoTerrenoRepository;
import isa.jima.ventas.repository.TipoTerrenoRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SubtipoTerrenoService {

    private final SubtipoTerrenoRepository subtipoTerrenoRepository;
    private final TipoTerrenoRepository tipoTerrenoRepository;

    @Transactional(readOnly = true)
    public List<SubtipoTerrenoResponse> listar() {
        return subtipoTerrenoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubtipoTerrenoResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public SubtipoTerrenoResponse crear(SubtipoTerrenoRequest request) {
        SubtipoTerreno subtipoTerreno = new SubtipoTerreno();
        mapearRequest(subtipoTerreno, request);
        return toResponse(subtipoTerrenoRepository.save(subtipoTerreno));
    }

    public SubtipoTerrenoResponse actualizar(Integer id, SubtipoTerrenoRequest request) {
        SubtipoTerreno subtipoTerreno = buscarPorId(id);
        mapearRequest(subtipoTerreno, request);
        return toResponse(subtipoTerrenoRepository.save(subtipoTerreno));
    }

    public void eliminar(Integer id) {
        if (!subtipoTerrenoRepository.existsById(id)) {
            throw new ResourceNotFoundException("SubtipoTerreno no encontrado: " + id);
        }
        subtipoTerrenoRepository.deleteById(id);
    }

    private SubtipoTerreno buscarPorId(Integer id) {
        return subtipoTerrenoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubtipoTerreno no encontrado: " + id));
    }

    private void mapearRequest(SubtipoTerreno subtipoTerreno, SubtipoTerrenoRequest request) {
        subtipoTerreno.setNombre(request.nombre());
        subtipoTerreno.setTipoTerreno(buscarTipoTerreno(request.idTipoTerreno()));
    }

    private TipoTerreno buscarTipoTerreno(Integer idTipoTerreno) {
        return tipoTerrenoRepository.findById(idTipoTerreno)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTerreno no encontrado: " + idTipoTerreno));
    }

    private SubtipoTerrenoResponse toResponse(SubtipoTerreno subtipoTerreno) {
        TipoTerreno tipoTerreno = subtipoTerreno.getTipoTerreno();
        return new SubtipoTerrenoResponse(
                subtipoTerreno.getIdSubtipoTerreno(),
                subtipoTerreno.getNombre(),
                tipoTerreno.getIdTipoTerreno(),
                tipoTerreno.getNombre());
    }
}
