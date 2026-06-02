package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.MunicipioRequest;
import isa.jima.ventas.dto.response.MunicipioResponse;
import isa.jima.ventas.entity.Municipio;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MunicipioService {

    private final MunicipioRepository municipioRepository;

    @Transactional(readOnly = true)
    public List<MunicipioResponse> listar() {
        return municipioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MunicipioResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public MunicipioResponse crear(MunicipioRequest request) {
        Municipio municipio = new Municipio();
        municipio.setNombre(request.nombre());
        return toResponse(municipioRepository.save(municipio));
    }

    public MunicipioResponse actualizar(Integer id, MunicipioRequest request) {
        Municipio municipio = buscarPorId(id);
        municipio.setNombre(request.nombre());
        return toResponse(municipioRepository.save(municipio));
    }

    public void eliminar(Integer id) {
        if (!municipioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Municipio no encontrado: " + id);
        }
        municipioRepository.deleteById(id);
    }

    private Municipio buscarPorId(Integer id) {
        return municipioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado: " + id));
    }

    private MunicipioResponse toResponse(Municipio municipio) {
        return new MunicipioResponse(municipio.getIdMunicipios(), municipio.getNombre());
    }
}
