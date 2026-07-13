package isa.jima.ventas.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.GastoFijo;
import isa.jima.ventas.entity.enums.CategoriaGastoFijo;
import isa.jima.ventas.entity.enums.EstatusGastoFijo;

@Repository
public interface GastoFijoRepository extends JpaRepository<GastoFijo, Integer> {

    @Query("SELECT g FROM GastoFijo g WHERE g.mes = :mes AND g.anio = :anio ORDER BY g.descripcion ASC")
    List<GastoFijo> buscarPorMesYAnio(@Param("mes") int mes, @Param("anio") int anio);

    @Query("SELECT g FROM GastoFijo g WHERE g.categoria = :categoria AND g.mes = :mes AND g.anio = :anio")
    List<GastoFijo> findByCategoria(
            @Param("categoria") CategoriaGastoFijo categoria,
            @Param("mes") int mes,
            @Param("anio") int anio);

    @Query("SELECT g FROM GastoFijo g WHERE g.estatus = :estatus AND g.mes = :mes AND g.anio = :anio")
    List<GastoFijo> findByEstatus(
            @Param("estatus") EstatusGastoFijo estatus,
            @Param("mes") int mes,
            @Param("anio") int anio);

    @Query("SELECT g FROM GastoFijo g WHERE g.mes = :mes AND g.anio = :anio AND g.excedente > 0 ORDER BY g.excedente DESC")
    List<GastoFijo> buscarConExcedente(@Param("mes") int mes, @Param("anio") int anio);

    @Query("SELECT g FROM GastoFijo g WHERE g.mes = :mes AND g.anio = :anio AND g.resto > 0 ORDER BY g.resto DESC")
    List<GastoFijo> buscarConResto(@Param("mes") int mes, @Param("anio") int anio);

    @Query("SELECT g FROM GastoFijo g WHERE g.mes = :mes AND g.anio = :anio AND g.ahorro > 0 ORDER BY g.ahorro DESC")
    List<GastoFijo> buscarConAhorro(@Param("mes") int mes, @Param("anio") int anio);

    @Override
    List<GastoFijo> findAll(Sort sort);
}
