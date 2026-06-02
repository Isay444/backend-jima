package isa.jima.ventas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.GastoDiario;

@Repository
public interface GastoDiariosRepository extends JpaRepository<GastoDiario, Integer> {

    List<GastoDiario> findByFechaBetweenOrderByFechaDesc(LocalDate inicio, LocalDate fin);
}
