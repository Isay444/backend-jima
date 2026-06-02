package isa.jima.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
}
