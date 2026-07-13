package isa.jima.ventas.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.ZonaEjidal;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaEjidalRepository extends JpaRepository<ZonaEjidal, Integer> {

    Optional<ZonaEjidal> findByNombre(String nombre);
    @Override
    List<ZonaEjidal> findAll(Sort sort);
}
