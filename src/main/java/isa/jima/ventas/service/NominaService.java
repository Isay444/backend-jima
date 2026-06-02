package isa.jima.ventas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.NominaRequest;
import isa.jima.ventas.dto.response.NominaResponse;
import isa.jima.ventas.dto.response.ResumenNominaResponse;
import isa.jima.ventas.entity.Nomina;
import isa.jima.ventas.entity.Trabajador;
import isa.jima.ventas.entity.Usuario;
import isa.jima.ventas.entity.enums.EstatusPago;
import isa.jima.ventas.entity.enums.Periodicidad;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.NominaRepository;
import isa.jima.ventas.repository.TrabajadorRepository;
import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NominaService {

    private final NominaRepository nominaRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<NominaResponse> listar() {
        return nominaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NominaResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public NominaResponse crear(NominaRequest request) {
        validarNomina(request);
        Nomina nomina = new Nomina();
        mapearRequest(nomina, request);
        return toResponse(nominaRepository.save(nomina));
    }

    public NominaResponse actualizar(Integer id, NominaRequest request) {
        validarNomina(request);
        Nomina nomina = buscarPorId(id);
        mapearRequest(nomina, request);
        return toResponse(nominaRepository.save(nomina));
    }

    public void eliminar(Integer id) {
        if (!nominaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nomina no encontrada: " + id);
        }
        nominaRepository.deleteById(id);
    }

    public NominaResponse marcarComoPagado(Integer id) {
        Nomina nomina = buscarPorId(id);
        if (nomina.getEstatusPago() == EstatusPago.PAGADO) {
            throw new BusinessException("La nómina ya se encuentra pagada");
        }
        nomina.setEstatusPago(EstatusPago.PAGADO);
        return toResponse(nominaRepository.save(nomina));
    }

    @Transactional(readOnly = true)
    public ResumenNominaResponse calcularResumen(LocalDate inicio, LocalDate fin) {
        BigDecimal totalGeneral = nominaRepository.sumSueldoBetween(inicio, fin);
        BigDecimal totalPagado = nominaRepository.sumSueldoByEstatusBetween(EstatusPago.PAGADO, inicio, fin);
        BigDecimal totalPendiente = nominaRepository.sumSueldoByEstatusBetween(EstatusPago.PENDIENTE, inicio, fin);
        return new ResumenNominaResponse(totalGeneral, totalPagado, totalPendiente);
    }

    @Transactional(readOnly = true)
    public List<NominaResponse> buscarPorRangoFecha(LocalDate inicio, LocalDate fin) {
        if (inicio.isAfter(fin)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return nominaRepository.findByFechaPagoBetweenOrderByFechaPagoDesc(inicio, fin).stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarNomina(NominaRequest request) {
        if (request.idTrabajador() == null) {
            throw new BusinessException("El trabajador es obligatorio");
        }
        if (request.sueldo() == null || request.sueldo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El sueldo debe ser mayor a 0");
        }
        if (request.periodicidad() == null) {
            throw new BusinessException("La periodicidad es obligatoria");
        }
        if (request.metodoPago() == null) {
            throw new BusinessException("El método de pago es obligatorio");
        }
        if (request.fechaPago() == null) {
            throw new BusinessException("La fecha de pago es obligatoria");
        }
        if (request.estatusPago() == null) {
            throw new BusinessException("El estatus de pago es obligatorio");
        }
    }

    private LocalDate[] calcularPeriodo(LocalDate fechaPago, Periodicidad periodicidad) {
        return switch (periodicidad) {
            case SEMANAL -> new LocalDate[] {fechaPago.minusDays(6), fechaPago};
            case QUINCENAL -> new LocalDate[] {fechaPago.minusDays(14), fechaPago};
            case MENSUAL -> new LocalDate[] {
                fechaPago.withDayOfMonth(1),
                fechaPago.withDayOfMonth(fechaPago.lengthOfMonth())
            };
        };
    }

    private void mapearRequest(Nomina nomina, NominaRequest request) {
        nomina.setTrabajador(buscarTrabajador(request.idTrabajador()));
        nomina.setSueldo(request.sueldo());
        nomina.setPeriodicidad(request.periodicidad());
        nomina.setMetodoPago(request.metodoPago());
        nomina.setFechaPago(request.fechaPago());
        nomina.setEstatusPago(request.estatusPago());
        nomina.setObservaciones(request.observaciones());
        nomina.setUsuario(buscarUsuario(request.idUsuario()));

        LocalDate periodoInicio = request.periodoInicio();
        LocalDate periodoFin = request.periodoFin();
        if (periodoInicio == null || periodoFin == null) {
            LocalDate[] periodo = calcularPeriodo(request.fechaPago(), request.periodicidad());
            periodoInicio = periodo[0];
            periodoFin = periodo[1];
        }
        nomina.setPeriodoInicio(periodoInicio);
        nomina.setPeriodoFin(periodoFin);
    }

    private Nomina buscarPorId(Integer id) {
        return nominaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nomina no encontrada: " + id));
    }

    private Trabajador buscarTrabajador(Integer idTrabajador) {
        return trabajadorRepository.findById(idTrabajador)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + idTrabajador));
    }

    private Usuario buscarUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + idUsuario));
    }

    private NominaResponse toResponse(Nomina nomina) {
        Trabajador trabajador = nomina.getTrabajador();
        Usuario usuario = nomina.getUsuario();
        String nombreTrabajador = trabajador.getNombre() + " " + trabajador.getApellidoPaterno();
        return new NominaResponse(
                nomina.getIdNomina(),
                trabajador.getIdTrabajador(),
                nombreTrabajador,
                nomina.getSueldo(),
                nomina.getPeriodicidad(),
                nomina.getMetodoPago(),
                nomina.getFechaPago(),
                nomina.getPeriodoInicio(),
                nomina.getPeriodoFin(),
                nomina.getEstatusPago(),
                nomina.getObservaciones(),
                usuario.getIdUsuario(),
                usuario.getNombre(),
                nomina.getFechaRegistro());
    }
}
