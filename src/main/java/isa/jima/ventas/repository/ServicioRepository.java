package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
}
