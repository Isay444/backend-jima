package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
}
