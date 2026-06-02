package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.ZonaEjidal;

@Repository
public interface ZonaEjidalRepository extends JpaRepository<ZonaEjidal, Integer> {
}
