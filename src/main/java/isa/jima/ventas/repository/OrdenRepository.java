// OrdenRepository.java
package isa.jima.ventas.repository;

import isa.jima.ventas.entity.Orden;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    // Bloqueo pesimista delegado a MySQL mediante FOR UPDATE
    @Query(value = "SELECT MAX(CAST(folio AS UNSIGNED)) FROM orden WHERE folio REGEXP '^[0-9]+$' FOR UPDATE", nativeQuery = true)
    Optional<Long> findMaxFolioNumerico();

    boolean existsByFolio(String folio);

    @Override
    List<Orden> findAll(Sort sort);
}