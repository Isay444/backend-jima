package isa.jima.ventas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import isa.jima.ventas.dto.request.GastoFijoRequest;
import isa.jima.ventas.dto.response.GastoFijoResponse;
import isa.jima.ventas.entity.GastoFijo;
import isa.jima.ventas.entity.Usuario;
import isa.jima.ventas.entity.enums.CategoriaGastoFijo;
import isa.jima.ventas.entity.enums.EstatusGastoFijo;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.CatalogoDescripcionGastoRepository;
import isa.jima.ventas.repository.GastoFijoRepository;
import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GastoFijoService {

    private final GastoFijoRepository gastoFijoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CatalogoDescripcionGastoRepository catalogoDescripcionGastoRepository;

    @Transactional(readOnly = true)
    public List<GastoFijoResponse> listar() {
        return gastoFijoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GastoFijoResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public GastoFijoResponse crear(GastoFijoRequest request) {
        validarGastoFijo(request);
        GastoFijo gastoFijo = new GastoFijo();
        mapearRequest(gastoFijo, request);
        return toResponse(gastoFijoRepository.save(gastoFijo));
    }

    public GastoFijoResponse actualizar(Integer id, GastoFijoRequest request) {
        validarGastoFijo(request);
        GastoFijo gastoFijo = buscarPorId(id);
        mapearRequest(gastoFijo, request);
        return toResponse(gastoFijoRepository.save(gastoFijo));
    }

    public void eliminar(Integer id) {
        if (!gastoFijoRepository.existsById(id)) {
            throw new ResourceNotFoundException("GastoFijo no encontrado: " + id);
        }
        gastoFijoRepository.deleteById(id);
    }

    public GastoFijoResponse marcarListoParaPagar(Integer id) {
        GastoFijo gastoFijo = buscarPorId(id);
        gastoFijo.setEstatus(EstatusGastoFijo.LISTO_PARA_PAGAR);
        return toResponse(gastoFijoRepository.save(gastoFijo));
    }

    @Transactional(readOnly = true)
    public List<GastoFijoResponse> buscarDelMesActual() {
        LocalDate hoy = LocalDate.now();
        return buscarPorMesYAnio(hoy.getMonthValue(), hoy.getYear());
    }

    @Transactional(readOnly = true)
    public List<GastoFijoResponse> buscarPorMesYAnio(int mes, int anio) {
        return gastoFijoRepository.buscarPorMesYAnio(mes, anio).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> obtenerResumenMensual(int mes, int anio) {
        List<GastoFijoResponse> gastos = buscarPorMesYAnio(mes, anio);
        Map<String, BigDecimal> resumen = new LinkedHashMap<>();
        resumen.put("totalEstimado", calcularTotalEstimado(gastos));
        resumen.put("totalReal", calcularTotalReal(gastos));
        resumen.put("totalAhorro", calcularTotalAhorro(gastos));
        resumen.put("totalExcedente", calcularTotalExcedente(gastos));
        resumen.put("totalResto", calcularTotalResto(gastos));
        return resumen;
    }

    @Transactional(readOnly = true)
    public List<String> obtenerDescripcionesUnicas() {
        return catalogoDescripcionGastoRepository.findByActivoTrue().stream()
                .map(c -> c.getDescripcion())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GastoFijoResponse> buscarConExcedente(int mes, int anio) {
        return gastoFijoRepository.buscarConExcedente(mes, anio).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GastoFijoResponse> buscarConAhorro(int mes, int anio) {
        return gastoFijoRepository.buscarConAhorro(mes, anio).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GastoFijoResponse> buscarConResto(int mes, int anio) {
        return gastoFijoRepository.buscarConResto(mes, anio).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalEstimado(List<GastoFijoResponse> gastos) {
        return gastos.stream()
                .map(GastoFijoResponse::montoEstimado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalReal(List<GastoFijoResponse> gastos) {
        return gastos.stream()
                .map(GastoFijoResponse::montoReal)
                .filter(monto -> monto != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalAhorro(List<GastoFijoResponse> gastos) {
        return gastos.stream()
                .map(GastoFijoResponse::ahorro)
                .filter(monto -> monto != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalExcedente(List<GastoFijoResponse> gastos) {
        return gastos.stream()
                .map(GastoFijoResponse::excedente)
                .filter(monto -> monto != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalResto(List<GastoFijoResponse> gastos) {
        return gastos.stream()
                .map(GastoFijoResponse::resto)
                .filter(monto -> monto != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Map<CategoriaGastoFijo, BigDecimal> calcularTotalesPorCategoria(List<GastoFijoResponse> gastos) {
        return gastos.stream()
                .collect(Collectors.groupingBy(
                        GastoFijoResponse::categoria,
                        Collectors.mapping(
                                GastoFijoResponse::montoEstimado,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    private void validarGastoFijo(GastoFijoRequest request) {
        if (!StringUtils.hasText(request.descripcion())) {
            throw new BusinessException("La descripción es obligatoria");
        }
        if (request.categoria() == null) {
            throw new BusinessException("La categoría es obligatoria");
        }
        if (request.montoEstimado() == null || request.montoEstimado().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El monto estimado debe ser mayor o igual a 0");
        }
        if (request.provision() == null || request.provision().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("La provisión debe ser mayor o igual a 0");
        }
        if (request.mes() == null || request.mes() < 1 || request.mes() > 12) {
            throw new BusinessException("El mes debe estar entre 1 y 12");
        }
        if (request.anio() == null || request.anio() < 2000) {
            throw new BusinessException("El año debe ser mayor o igual a 2000");
        }
        if (request.montoReal() != null && request.montoReal().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El monto real debe ser mayor o igual a 0");
        }
    }

    private GastoFijo buscarPorId(Integer id) {
        return gastoFijoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GastoFijo no encontrado: " + id));
    }

    private void mapearRequest(GastoFijo gastoFijo, GastoFijoRequest request) {
        gastoFijo.setDescripcion(request.descripcion());
        gastoFijo.setCategoria(request.categoria());
        gastoFijo.setMontoEstimado(request.montoEstimado());
        gastoFijo.setMontoReal(request.montoReal());
        gastoFijo.setProvision(request.provision());
        gastoFijo.setEstatus(request.estatus());
        gastoFijo.setMes(request.mes());
        gastoFijo.setAnio(request.anio());
        if (request.idUsuario() != null) {
            gastoFijo.setUsuario(buscarUsuario(request.idUsuario()));
        } else {
            gastoFijo.setUsuario(null);
        }
    }

    private Usuario buscarUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + idUsuario));
    }

    private GastoFijoResponse toResponse(GastoFijo gastoFijo) {
        Usuario usuario = gastoFijo.getUsuario();
        return new GastoFijoResponse(
                gastoFijo.getIdGastoFijo(),
                gastoFijo.getDescripcion(),
                gastoFijo.getCategoria(),
                gastoFijo.getMontoEstimado(),
                gastoFijo.getMontoReal(),
                gastoFijo.getProvision(),
                gastoFijo.getAhorro(),
                gastoFijo.getExcedente(),
                gastoFijo.getResto(),
                gastoFijo.getEstatus(),
                gastoFijo.getMes(),
                gastoFijo.getAnio(),
                gastoFijo.getFechaRegistro(),
                gastoFijo.getFechaActualizacion(),
                usuario != null ? usuario.getIdUsuario() : null);
    }
}
