package isa.jima.ventas.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.GastoDiarioRequest;
import isa.jima.ventas.dto.response.GastoDiarioResponse;
import isa.jima.ventas.entity.GastoDiario;
import isa.jima.ventas.entity.Usuario;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.GastoDiariosRepository;
import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GastoDiarioService {

    private final GastoDiariosRepository gastoDiariosRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<GastoDiarioResponse> listar() {
        return gastoDiariosRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GastoDiarioResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public GastoDiarioResponse crear(GastoDiarioRequest request) {
        GastoDiario gastoDiario = new GastoDiario();
        mapearRequest(gastoDiario, request);
        return toResponse(gastoDiariosRepository.save(gastoDiario));
    }

    public GastoDiarioResponse actualizar(Integer id, GastoDiarioRequest request) {
        GastoDiario gastoDiario = buscarPorId(id);
        mapearRequest(gastoDiario, request);
        return toResponse(gastoDiariosRepository.save(gastoDiario));
    }

    public void eliminar(Integer id) {
        if (!gastoDiariosRepository.existsById(id)) {
            throw new ResourceNotFoundException("GastoDiario no encontrado: " + id);
        }
        gastoDiariosRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<GastoDiarioResponse> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return gastoDiariosRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin).stream()
                .map(this::toResponse)
                .toList();
    }

    private GastoDiario buscarPorId(Integer id) {
        return gastoDiariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GastoDiario no encontrado: " + id));
    }

    private void mapearRequest(GastoDiario gastoDiario, GastoDiarioRequest request) {
        gastoDiario.setFecha(request.fecha());
        gastoDiario.setDescripcion(request.descripcion());
        gastoDiario.setEsMateriaPrima(request.esMateriaPrima());
        gastoDiario.setTipoMateriaPrima(request.tipoMateriaPrima());
        gastoDiario.setMetodoPago(request.metodoPago());
        gastoDiario.setMonto(request.monto());
        gastoDiario.setTieneRecibo(request.tieneRecibo());
        gastoDiario.setCategoria(request.categoria());
        gastoDiario.setUsuario(buscarUsuario(request.idUsuario()));
    }

    private Usuario buscarUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + idUsuario));
    }

    private GastoDiarioResponse toResponse(GastoDiario gastoDiario) {
        Usuario usuario = gastoDiario.getUsuario();
        return new GastoDiarioResponse(
                gastoDiario.getIdGasto(),
                gastoDiario.getFecha(),
                gastoDiario.getDescripcion(),
                gastoDiario.getEsMateriaPrima(),
                gastoDiario.getTipoMateriaPrima(),
                gastoDiario.getMetodoPago(),
                gastoDiario.getMonto(),
                gastoDiario.getTieneRecibo(),
                gastoDiario.getCategoria(),
                usuario.getIdUsuario(),
                usuario.getNombre(),
                gastoDiario.getFechaRegistro());
    }
}
