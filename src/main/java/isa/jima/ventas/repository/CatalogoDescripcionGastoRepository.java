package isa.jima.ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.CatalogoDescripcionGasto;

@Repository
public interface CatalogoDescripcionGastoRepository extends JpaRepository<CatalogoDescripcionGasto, Integer> {

    List<CatalogoDescripcionGasto> findByActivoTrue();
}
