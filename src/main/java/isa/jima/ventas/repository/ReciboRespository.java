package isa.jima.ventas.repository;

import isa.jima.ventas.entity.Recibo;
import isa.jima.ventas.entity.enums.TipoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReciboRespository extends JpaRepository<Recibo, Integer> {
    List<Recibo> findByOrdenIdOrden(Integer idOrden);

    boolean existsByOrdenIdOrdenAndTipoPago(Integer idOrden, TipoPago tipoPago);

    @Query("SELECT COALESCE(SUM(r.monto), 0) FROM Recibo r WHERE r.orden.idOrden = :idOrden AND r.confirmado = true")
    BigDecimal sumMontoCOnfirmadoByOrden(@Param("idOrden") Integer idOrden);

    Optional<Recibo> findByOrdenIdOrdenAndTipoPago(Integer idOrden, TipoPago tipoPago);
}
