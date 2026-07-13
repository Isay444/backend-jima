package isa.jima.ventas.service;

import isa.jima.ventas.dto.request.OrdenRequest;
import isa.jima.ventas.dto.response.OrdenResponse;
import isa.jima.ventas.entity.Orden;
import isa.jima.ventas.entity.enums.EstadoPlano;
import isa.jima.ventas.entity.enums.EstatusOrden;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenService {
    private final OrdenRepository ordenRepository;
    private final FolioService folioService;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final SubtipoTerrenoRepository subtipoTerrenoRepository;
    private final MunicipioRepository municipioRepository;
    private final ZonaEjidalRepository zonaEjidalRepository;
    private final IngenieroRepository ingenieroRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<OrdenResponse> listar(){
        Sort sortByFolio = Sort.by(Sort.Direction.DESC, "folio");
        return ordenRepository.findAll(sortByFolio).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrdenResponse obtener(Integer id){
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
        return toResponse(orden);
    }

    public OrdenResponse crear(OrdenRequest request){
        Orden orden = new Orden();

        // Mapeo de relaciones seguras
        orden.setCliente(clienteRepository.findById(request.idCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado")));
        orden.setServicio(servicioRepository.findById(request.idServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado")));
        orden.setSubtipoTerreno(subtipoTerrenoRepository.findById(request.idSubtipoTerreno())
                .orElseThrow(() -> new ResourceNotFoundException("Subtipo de terreno no encontrado")));
        orden.setMunicipio(municipioRepository.findById(request.idMunicipio())
                .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado")));
        orden.setZonaEjidal(zonaEjidalRepository.findById(request.idZonaEjidal())
                .orElseThrow(() -> new ResourceNotFoundException("Zona ejidal no encontrada")));
        orden.setIngeniero(ingenieroRepository.findById(request.idIngeniero())
                .orElseThrow(() -> new ResourceNotFoundException("Ingeniero no encontrado")));
        orden.setUsuario(usuarioRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));

        // Set de datos base
        orden.setFolio(folioService.generarFolio());
        orden.setFecha(request.fecha());
        orden.setObservaciones(request.observaciones());
        orden.setMontoTotal(request.montoTotal());
        orden.setSolicitaFactura(request.solicitaFactura());
        orden.setRequierePlano(request.requierePlano());

        if (request.estadoPlano() != null) {
            orden.setEstadoPlano(request.estadoPlano());
        } else {
            orden.setEstadoPlano(EstadoPlano.INDEFINIDO);
        }

        // Lógica de comisiones
        orden.setTieneComision(request.tieneComision());
        orden.setComisionista(request.comisionista());
        orden.setPorcentajeComision(request.porcentajeComision());

        if (Boolean.TRUE.equals(request.tieneComision()) && request.porcentajeComision() != null) {
            BigDecimal comision = request.montoTotal()
                    .multiply(request.porcentajeComision())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            orden.setMontoComision(comision);
        } else {
            orden.setMontoComision(BigDecimal.ZERO);
        }


        // Automatización de fechas
        orden.setCalcFechaLevantamientoAuto(request.calcFechaLevantamientoAuto());
        orden.setCalcFechaEntregaAuto(request.calcFechaEntregaAuto());
        orden.setDiasEntregaPlano(request.diasEntregaPlano());
        orden.setFechaLevantamiento(request.fechaLevantamiento());
        orden.setFechaEntregaPlano(request.fechaEntregaPlano());

        // Valores iniciales de negocio
        orden.setEstatus(EstatusOrden.ACTIVA);
        orden.setSaldoRestante(request.montoTotal());

        Orden savedOrden = ordenRepository.save(orden);
        return toResponse(savedOrden);

    }

    public OrdenResponse actualizar(Integer id, OrdenRequest request) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (orden.getEstatus() == EstatusOrden.CANCELADA) {
            throw new BusinessException("No se puede editar una orden cancelada.");
        }

        // Actualización de entidades foráneas (omitidas por brevedad, asumen la misma validación que en crear)
        orden.setCliente(clienteRepository.findById(request.idCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado")));
        orden.setServicio(servicioRepository.findById(request.idServicio())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado")));
        orden.setSubtipoTerreno(subtipoTerrenoRepository.findById(request.idSubtipoTerreno())
                .orElseThrow(() -> new ResourceNotFoundException("Subtipo de terreno no encontrado")));
        orden.setMunicipio(municipioRepository.findById(request.idMunicipio())
                .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado")));
        orden.setZonaEjidal(zonaEjidalRepository.findById(request.idZonaEjidal())
                .orElseThrow(() -> new ResourceNotFoundException("Zona ejidal no encontrada")));
        orden.setIngeniero(ingenieroRepository.findById(request.idIngeniero())
                .orElseThrow(() -> new ResourceNotFoundException("Ingeniero no encontrado")));

        orden.setFecha(request.fecha());
        orden.setObservaciones(request.observaciones());
        orden.setMontoTotal(request.montoTotal());
        orden.setSolicitaFactura(request.solicitaFactura());
        orden.setRequierePlano(request.requierePlano());

        if (request.estadoPlano() != null) {
            orden.setEstadoPlano(request.estadoPlano());
        }

        // Recálculo de comisiones si es necesario
        boolean comisionCambio = !orden.getTieneComision().equals(request.tieneComision()) ||
                (request.porcentajeComision() != null && !request.porcentajeComision().equals(orden.getPorcentajeComision()));

        orden.setTieneComision(request.tieneComision());
        orden.setComisionista(request.comisionista());
        orden.setPorcentajeComision(request.porcentajeComision());

        if (comisionCambio) {
            if (Boolean.TRUE.equals(request.tieneComision()) && request.porcentajeComision() != null) {
                BigDecimal comision = request.montoTotal()
                        .multiply(request.porcentajeComision())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                orden.setMontoComision(comision);
            } else {
                orden.setMontoComision(BigDecimal.ZERO);
            }
        }

        orden.setCalcFechaLevantamientoAuto(request.calcFechaLevantamientoAuto());
        orden.setCalcFechaEntregaAuto(request.calcFechaEntregaAuto());
        orden.setDiasEntregaPlano(request.diasEntregaPlano());
        orden.setFechaLevantamiento(request.fechaLevantamiento());
        orden.setFechaEntregaPlano(request.fechaEntregaPlano());

        return toResponse(ordenRepository.save(orden));
    }

    public void eliminar(Integer id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        // Se asume que getRecibos() está mapeado en la entidad Orden
        if (orden.getRecibos() != null && !orden.getRecibos().isEmpty()) {
            throw new BusinessException("No se puede eliminar una orden con recibos registrados");
        }

        ordenRepository.delete(orden);
    }

    public OrdenResponse cancelar(Integer id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (orden.getEstatus() == EstatusOrden.CANCELADA || orden.getEstatus() == EstatusOrden.TERMINADA) {
            throw new BusinessException("La orden ya se encuentra en estado " + orden.getEstatus());
        }

        orden.setEstatus(EstatusOrden.CANCELADA);
        return toResponse(ordenRepository.save(orden));
    }

    public String previewFolio() {
        return folioService.previewFolio();
    }

    private OrdenResponse toResponse(Orden orden){
        String apellidoM = orden.getCliente().getApellidoMaterno() != null ? orden.getCliente().getApellidoMaterno() : " ";
        String nombreCompletoCliente = orden.getCliente().getNombreS() + " " + orden.getCliente().getApellidoPaterno() + " " + apellidoM;
        OrdenResponse.ClienteInfo clienteInfo = new OrdenResponse.ClienteInfo(orden.getCliente().getIdClientes(), nombreCompletoCliente);
        OrdenResponse.IngenieroInfo ingenieroInfo = new OrdenResponse.IngenieroInfo(orden.getIngeniero().getIdIngeniero(), orden.getIngeniero().getNombre());
        OrdenResponse.ServicioInfo servicioInfo = new OrdenResponse.ServicioInfo(orden.getServicio().getIdServicio(), orden.getServicio().getNombre());
        OrdenResponse.SubtipoTerrenoInfo terrenoInfo = new OrdenResponse.SubtipoTerrenoInfo(orden.getSubtipoTerreno().getIdSubtipoTerreno(), orden.getSubtipoTerreno().getNombre());
        OrdenResponse.MunicipioInfo municipioInfo = new OrdenResponse.MunicipioInfo(orden.getMunicipio().getIdMunicipios(), orden.getMunicipio().getNombre());
        OrdenResponse.ZonaEjidalInfo zonaInfo = new OrdenResponse.ZonaEjidalInfo(orden.getZonaEjidal().getIdZonaEjidal(), orden.getZonaEjidal().getNombre());

        OrdenResponse.UsuarioInfo usuarioInfo = null;
        if (orden.getUsuario() != null){
            usuarioInfo = new OrdenResponse.UsuarioInfo(orden.getUsuario().getIdUsuario(), orden.getUsuario().getNombre());
        }

        return new OrdenResponse(
                orden.getIdOrden(),                     // 0
                orden.getFolio(),                       // 1
                orden.getEstatus(),                     // 2
                orden.getMontoTotal(),                  // 3
                orden.getSaldoRestante(),               // 4
                orden.getMontoComision(),               // 5
                orden.getSolicitaFactura(),             // 6
                orden.getRequierePlano(),               // 7
                orden.getEstadoPlano(),                 // 8
                orden.getTieneComision(),               // 9
                orden.getComisionista(),                // 10
                orden.getPorcentajeComision(),          // 11
                orden.getCalcFechaLevantamientoAuto(),  // 12
                orden.getCalcFechaEntregaAuto(),        // 13
                orden.getDiasEntregaPlano(),            // 14
                orden.getFecha(),                       // 15
                orden.getFechaLevantamiento(),          // 16
                orden.getFechaEntregaPlano(),           // 17
                orden.getRutaPlano(),                   // 18
                orden.getObservaciones(),               // 19
                clienteInfo,                            // 20
                ingenieroInfo,                          // 21
                servicioInfo,                           // 22
                terrenoInfo,                            // 23
                municipioInfo,                          // 24
                zonaInfo,                               // 25
                usuarioInfo                             // 26
        );
    }
}
