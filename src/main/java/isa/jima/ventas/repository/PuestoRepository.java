package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Puesto;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Integer> {
}
