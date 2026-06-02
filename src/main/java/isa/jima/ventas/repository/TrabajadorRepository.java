package isa.jima.ventas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Trabajador;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {

    List<Trabajador> findByActivoTrue();

    List<Trabajador> findByNombreContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(
            String nombre, String apellidoPaterno);
}
