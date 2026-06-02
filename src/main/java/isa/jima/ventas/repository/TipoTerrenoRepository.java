package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.TipoTerreno;

@Repository
public interface TipoTerrenoRepository extends JpaRepository<TipoTerreno, Integer> {
}
