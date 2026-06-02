package isa.jima.ventas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Nomina;
import isa.jima.ventas.entity.Trabajador;
import isa.jima.ventas.entity.enums.EstatusPago;

@Repository
public interface NominaRepository extends JpaRepository<Nomina, Integer> {

    List<Nomina> findByFechaPagoBetweenOrderByFechaPagoDesc(LocalDate inicio, LocalDate fin);

    List<Nomina> findByTrabajador(Trabajador trabajador);

    List<Nomina> findByEstatusPagoAndFechaPagoBetween(
            EstatusPago estatus, LocalDate inicio, LocalDate fin);

    @Query("SELECT COALESCE(SUM(n.sueldo), 0) FROM Nomina n WHERE n.fechaPago BETWEEN :inicio AND :fin")
    BigDecimal sumSueldoBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(n.sueldo), 0) FROM Nomina n WHERE n.fechaPago BETWEEN :inicio AND :fin AND n.estatusPago = :estatus")
    BigDecimal sumSueldoByEstatusBetween(
            @Param("estatus") EstatusPago estatus,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);
}
