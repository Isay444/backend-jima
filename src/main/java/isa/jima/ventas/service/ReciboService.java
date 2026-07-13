package isa.jima.ventas.service;

import isa.jima.ventas.dto.request.ReciboRequest;
import isa.jima.ventas.dto.response.ReciboResponse;
import isa.jima.ventas.entity.Orden;
import isa.jima.ventas.entity.Recibo;
import isa.jima.ventas.entity.enums.EstatusOrden;
import isa.jima.ventas.entity.enums.TipoPago;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.OrdenRepository;
import isa.jima.ventas.repository.ReciboRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReciboService {

    private final ReciboRespository reciboRepository;
    private final OrdenRepository ordenRepository;

    @Transactional(readOnly = true)
    public List<ReciboResponse> listarPorOrden(Integer idOrden){
        if(!ordenRepository.existsById(idOrden)){
            throw new ResourceNotFoundException("Orden no encontrada");
        }
        return reciboRepository.findByOrdenIdOrden(idOrden).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReciboResponse obtener(Integer id){
        Recibo recibo = reciboRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Recibo no encontrado"));
        return toResponse(recibo);
    }

    public ReciboResponse crear(ReciboRequest request){
        // Paso 1 -  Buscar la orden
        Orden orden = ordenRepository.findById(request.idOrden())
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (orden.getEstatus() == EstatusOrden.CANCELADA){
            throw new BusinessException("No se puede agregar recibos a una orden CANCELADA");
        }

        TipoPago tipoPagoReq = request.tipoPago();
        Integer idOrden = orden.getIdOrden();

        // Paso 2 - Reglas de negocio del tipo de recibo
        boolean esElPrimero = !reciboRepository.existsByOrdenIdOrdenAndTipoPago(idOrden, TipoPago.ANTICIPO)
                && reciboRepository.findByOrdenIdOrden(idOrden).isEmpty();

        if (esElPrimero && tipoPagoReq != TipoPago.ANTICIPO) {
            throw new BusinessException("El primer recibo debe ser de tipo ANTICIPO");
        }
        if (!esElPrimero && tipoPagoReq == TipoPago.ANTICIPO) {
            throw new BusinessException("Ya existe un ANTICIPO para esta orden");
        }
        if (tipoPagoReq == TipoPago.SALDO && reciboRepository.existsByOrdenIdOrdenAndTipoPago(idOrden, TipoPago.SALDO)) {
            throw new BusinessException("Ya existe un SALDO para esta orden");
        }

        // Paso 3 — Validación anti-sobrepago
        BigDecimal sumaActual = reciboRepository.sumMontoCOnfirmadoByOrden(idOrden);
        if (Boolean.TRUE.equals(request.confirmado()) &&
                sumaActual.add(request.monto()).compareTo(orden.getMontoTotal()) > 0) {
            throw new BusinessException("El monto excede el total de la orden");
        }

        // Paso 4 — Guardar el recibo
        Recibo recibo = new Recibo();
        recibo.setOrden(orden);
        recibo.setFecha(request.fecha());
        recibo.setMonto(request.monto());
        recibo.setMetodoPago(request.metodoPago());
        recibo.setTipoPago(tipoPagoReq);
        recibo.setConfirmado(request.confirmado());

        if (request.banco() != null && request.metodoPago() != null) {
            recibo.setBanco(request.banco());
        }

        recibo = reciboRepository.save(recibo);

        // Paso 5 y 6 — Actualizar saldo y autómata
        actualizarSaldosYFechasOrden(orden, idOrden);

        // Paso 7 — Retornar
        return toResponse(recibo);
    }

    public void eliminar(Integer id) {
        Recibo recibo = reciboRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recibo no encontrado"));

        if (recibo.getTipoPago() == TipoPago.ANTICIPO) {
            throw new BusinessException("No se puede eliminar el anticipo de una orden");
        }
        if (Boolean.TRUE.equals(recibo.getConfirmado())) {
            throw new BusinessException("No se puede eliminar un recibo confirmado");
        }

        Orden orden = recibo.getOrden();
        Integer idOrden = orden.getIdOrden();

        reciboRepository.delete(recibo);

        // Recalcular saldos de la orden después de eliminar
        // Se llama a flush para garantizar que el SUM JPQL no cuente el recibo eliminado
        reciboRepository.flush();
        actualizarSaldosYFechasOrden(orden, idOrden);
    }

    // --- MÉTODOS PRIVADOS ---

    private void actualizarSaldosYFechasOrden(Orden orden, Integer idOrden) {
        // Paso 5 — Actualizar saldo de la orden
        BigDecimal nuevaSuma = reciboRepository.sumMontoCOnfirmadoByOrden(idOrden);
        orden.setSaldoRestante(orden.getMontoTotal().subtract(nuevaSuma));

        // Paso 6 — Autómata de fechas y estatus
        if (Boolean.TRUE.equals(orden.getCalcFechaLevantamientoAuto())) {
            reciboRepository.findByOrdenIdOrdenAndTipoPago(idOrden, TipoPago.ANTICIPO)
                    .ifPresent(anticipo -> orden.setFechaLevantamiento(anticipo.getFecha()));
        }

        if (Boolean.TRUE.equals(orden.getCalcFechaEntregaAuto()) &&
                Boolean.TRUE.equals(orden.getRequierePlano()) &&
                orden.getFechaLevantamiento() != null) {
            orden.setFechaEntregaPlano(orden.getFechaLevantamiento().plusDays(orden.getDiasEntregaPlano()));
        }

        if (orden.getSaldoRestante().compareTo(BigDecimal.ZERO) == 0) {
            orden.setEstatus(EstatusOrden.TERMINADA);
        } else {
            orden.setEstatus(EstatusOrden.ACTIVA);
        }

        ordenRepository.save(orden);
    }

    private ReciboResponse toResponse(Recibo recibo) {
        Orden orden = recibo.getOrden();

        ReciboResponse.OrdenInfo ordenInfo = new ReciboResponse.OrdenInfo(
                orden.getIdOrden(),
                orden.getFolio(),
                orden.getMontoTotal(),
                orden.getSaldoRestante()
        );

        return new ReciboResponse(
                recibo.getIdRecibo(),
                recibo.getFecha(),
                recibo.getMonto(),
                recibo.getMetodoPago(),
                recibo.getTipoPago(),
                recibo.getConfirmado(),
                recibo.getBanco(),
                ordenInfo
        );
    }
}
