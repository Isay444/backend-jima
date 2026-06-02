package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.SubtipoTerreno;

@Repository
public interface SubtipoTerrenoRepository extends JpaRepository<SubtipoTerreno, Integer> {
}
