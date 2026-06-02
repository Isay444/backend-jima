package isa.jima.ventas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isa.jima.ventas.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombre(String nombre);

    List<Usuario> findByActivoTrue();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.nombre = :rolNombre AND u.activo = true")
    long countByRolNombreAndActivoTrue(@Param("rolNombre") String rolNombre);

    boolean existsByRol_IdRol(Integer idRol);
}
