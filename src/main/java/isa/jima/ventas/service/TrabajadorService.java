package isa.jima.ventas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import isa.jima.ventas.dto.request.TrabajadorRequest;
import isa.jima.ventas.dto.response.TrabajadorResponse;
import isa.jima.ventas.entity.Area;
import isa.jima.ventas.entity.Puesto;
import isa.jima.ventas.entity.Trabajador;
import isa.jima.ventas.entity.Usuario;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.AreaRepository;
import isa.jima.ventas.repository.PuestoRepository;
import isa.jima.ventas.repository.TrabajadorRepository;
import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TrabajadorService {

    private static final Pattern RFC_PATTERN =
            Pattern.compile("^[A-Z&Ñ]{3,4}[0-9]{6}[A-Z0-9]{3}$");
    private static final Pattern CURP_PATTERN =
            Pattern.compile("^[A-Z]{4}[0-9]{6}[HM][A-Z]{5}[A-Z0-9]{2}$");

    private final TrabajadorRepository trabajadorRepository;
    private final PuestoRepository puestoRepository;
    private final AreaRepository areaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<TrabajadorResponse> listar() {
        Sort sortByNombre = Sort.by(Sort.Direction.ASC, "nombre");
        return trabajadorRepository.findAll(sortByNombre).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrabajadorResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public TrabajadorResponse crear(TrabajadorRequest request) {
        validarTrabajador(request);
        Trabajador trabajador = new Trabajador();
        mapearRequest(trabajador, request);
        return toResponse(trabajadorRepository.save(trabajador));
    }

    public TrabajadorResponse actualizar(Integer id, TrabajadorRequest request) {
        validarTrabajador(request);
        Trabajador trabajador = buscarPorId(id);
        mapearRequest(trabajador, request);
        return toResponse(trabajadorRepository.save(trabajador));
    }

    public void eliminar(Integer id) {
        if (!trabajadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trabajador no encontrado: " + id);
        }
        trabajadorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TrabajadorResponse> listarActivos() {
        return trabajadorRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TrabajadorResponse> buscarPorNombre(String nombre) {
        return trabajadorRepository
                .findByNombreContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(nombre, nombre)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TrabajadorResponse darDeBaja(Integer id, String motivo) {
        if (!StringUtils.hasText(motivo)) {
            throw new BusinessException("El motivo de baja es obligatorio");
        }
        Trabajador trabajador = buscarPorId(id);
        if (!Boolean.TRUE.equals(trabajador.getActivo())) {
            throw new BusinessException("El trabajador ya se encuentra dado de baja");
        }
        trabajador.setActivo(false);
        trabajador.setFechaBaja(LocalDate.now());
        trabajador.setMotivoBaja(motivo);
        return toResponse(trabajadorRepository.save(trabajador));
    }

    private void validarTrabajador(TrabajadorRequest request) {
        if (!StringUtils.hasText(request.nombre())) {
            throw new BusinessException("El nombre es obligatorio");
        }
        if (!StringUtils.hasText(request.apellidoPaterno())) {
            throw new BusinessException("El apellido paterno es obligatorio");
        }
        if (request.idPuesto() == null) {
            throw new BusinessException("El puesto es obligatorio");
        }
        if (request.idArea() == null) {
            throw new BusinessException("El área es obligatoria");
        }
        if (request.fechaAlta() == null) {
            throw new BusinessException("La fecha de alta es obligatoria");
        }
        if (request.fechaAlta().isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de alta no puede ser futura");
        }
        if (StringUtils.hasText(request.rfc())) {
            String rfc = request.rfc().trim().toUpperCase();
            if (rfc.length() < 12 || rfc.length() > 13 || !RFC_PATTERN.matcher(rfc).matches()) {
                throw new BusinessException("El RFC no tiene un formato válido");
            }
        }
        if (StringUtils.hasText(request.curp())) {
            String curp = request.curp().trim().toUpperCase();
            if (curp.length() != 18 || !CURP_PATTERN.matcher(curp).matches()) {
                throw new BusinessException("La CURP no tiene un formato válido");
            }
        }
    }

    private Trabajador buscarPorId(Integer id) {
        return trabajadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + id));
    }

    private void mapearRequest(Trabajador trabajador, TrabajadorRequest request) {
        trabajador.setNombre(request.nombre());
        trabajador.setApellidoPaterno(request.apellidoPaterno());
        trabajador.setApellidoMaterno(request.apellidoMaterno());
        trabajador.setPuesto(buscarPuesto(request.idPuesto()));
        trabajador.setArea(buscarArea(request.idArea()));
        trabajador.setRfc(request.rfc() != null ? request.rfc().trim().toUpperCase() : null);
        trabajador.setCurp(request.curp() != null ? request.curp().trim().toUpperCase() : null);
        trabajador.setTelefono(request.telefono());
        trabajador.setEmail(request.email());
        trabajador.setFechaAlta(request.fechaAlta());
        trabajador.setActivo(request.activo());
        if (request.idUsuario() != null) {
            trabajador.setUsuario(buscarUsuario(request.idUsuario()));
        } else {
            trabajador.setUsuario(null);
        }
    }

    private Puesto buscarPuesto(Integer idPuesto) {
        return puestoRepository.findById(idPuesto)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado: " + idPuesto));
    }

    private Area buscarArea(Integer idArea) {
        return areaRepository.findById(idArea)
                .orElseThrow(() -> new ResourceNotFoundException("Area no encontrada: " + idArea));
    }

    private Usuario buscarUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + idUsuario));
    }

    private TrabajadorResponse toResponse(Trabajador trabajador) {
        Puesto puesto = trabajador.getPuesto();
        Area area = trabajador.getArea();
        Usuario usuario = trabajador.getUsuario();
        return new TrabajadorResponse(
                trabajador.getIdTrabajador(),
                trabajador.getNombre(),
                trabajador.getApellidoPaterno(),
                trabajador.getApellidoMaterno(),
                puesto.getIdPuesto(),
                puesto.getNombre(),
                area.getIdArea(),
                area.getNombre(),
                trabajador.getRfc(),
                trabajador.getCurp(),
                trabajador.getTelefono(),
                trabajador.getEmail(),
                trabajador.getFechaAlta(),
                trabajador.getFechaBaja(),
                trabajador.getMotivoBaja(),
                trabajador.getActivo(),
                usuario != null ? usuario.getIdUsuario() : null,
                usuario != null ? usuario.getNombre() : null,
                trabajador.getFechaRegistro());
    }
}
