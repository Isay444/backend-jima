package isa.jima.ventas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.ServicioRequest;
import isa.jima.ventas.dto.response.ServicioResponse;
import isa.jima.ventas.entity.Servicio;
import isa.jima.ventas.entity.TipoServicio;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.ServicioRepository;
import isa.jima.ventas.repository.TipoServicioRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final TipoServicioRepository tipoServicioRepository;

    @Transactional(readOnly = true)
    public List<ServicioResponse> listar() {
        return servicioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public ServicioResponse crear(ServicioRequest request) {
        Servicio servicio = new Servicio();
        mapearRequest(servicio, request);
        return toResponse(servicioRepository.save(servicio));
    }

    public ServicioResponse actualizar(Integer id, ServicioRequest request) {
        Servicio servicio = buscarPorId(id);
        mapearRequest(servicio, request);
        return toResponse(servicioRepository.save(servicio));
    }

    public void eliminar(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Servicio no encontrado: " + id);
        }
        servicioRepository.deleteById(id);
    }

    private Servicio buscarPorId(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado: " + id));
    }

    private void mapearRequest(Servicio servicio, ServicioRequest request) {
        servicio.setNombre(request.nombre());
        servicio.setDescripcion(request.descripcion());
        servicio.setTipoServicio(buscarTipoServicio(request.idTipoServicio()));
    }

    private TipoServicio buscarTipoServicio(Integer idTipoServicio) {
        return tipoServicioRepository.findById(idTipoServicio)
                .orElseThrow(() -> new ResourceNotFoundException("TipoServicio no encontrado: " + idTipoServicio));
    }

    private ServicioResponse toResponse(Servicio servicio) {
        TipoServicio tipoServicio = servicio.getTipoServicio();
        return new ServicioResponse(
                servicio.getIdServicio(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                tipoServicio.getIdTipoServicio(),
                tipoServicio.getNombre());
    }
}
