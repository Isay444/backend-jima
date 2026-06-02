package isa.jima.ventas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.RolRequest;
import isa.jima.ventas.dto.response.PermisoResponse;
import isa.jima.ventas.dto.response.RolResponse;
import isa.jima.ventas.entity.Permiso;
import isa.jima.ventas.entity.Rol;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.PermisoRepository;
import isa.jima.ventas.repository.RolRepository;
import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RolService {

    private static final List<String> ROLES_SISTEMA = List.of(
            "ADMINISTRADOR", "SUPERVISOR", "OPERADOR", "CONTADOR", "VENDEDOR");

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PermisoRepository permisoRepository;

    @Transactional(readOnly = true)
    public List<RolResponse> listar() {
        return rolRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RolResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public RolResponse crear(RolRequest request) {
        String nombre = request.nombre().toUpperCase().trim();
        if (rolRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe un rol con ese nombre");
        }
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setDescripcion(request.descripcion());
        rol.setActivo(request.activo());
        rol.setPermisos(buscarPermisos(request.idPermisos()));
        return toResponse(rolRepository.save(rol));
    }

    public RolResponse actualizarPermisos(Integer id, List<Integer> idPermisos) {
        Rol rol = buscarPorId(id);
        if (esRolCritico(rol.getNombre())) {
            throw new BusinessException("No se pueden modificar los permisos del rol ADMINISTRADOR");
        }
        rol.setPermisos(buscarPermisos(idPermisos));
        return toResponse(rolRepository.save(rol));
    }

    public RolResponse activarDesactivar(Integer id) {
        Rol rol = buscarPorId(id);
        if (esRolCritico(rol.getNombre()) && Boolean.TRUE.equals(rol.getActivo())) {
            throw new BusinessException("No se puede desactivar el rol ADMINISTRADOR");
        }
        rol.setActivo(!rol.getActivo());
        return toResponse(rolRepository.save(rol));
    }

    public void eliminar(Integer id) {
        Rol rol = buscarPorId(id);
        if (esRolSistema(rol.getNombre())) {
            throw new BusinessException("No se pueden eliminar roles del sistema");
        }
        if (tieneUsuariosAsignados(id)) {
            throw new BusinessException("No se puede eliminar el rol porque tiene usuarios asignados");
        }
        rolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PermisoResponse> listarPermisos() {
        return permisoRepository.findAll().stream()
                .map(this::toPermisoResponse)
                .toList();
    }

    private boolean esRolSistema(String nombre) {
        return ROLES_SISTEMA.contains(nombre.toUpperCase());
    }

    private boolean esRolCritico(String nombre) {
        return nombre.equalsIgnoreCase("ADMINISTRADOR");
    }

    private boolean tieneUsuariosAsignados(Integer idRol) {
        return usuarioRepository.existsByRol_IdRol(idRol);
    }

    private Rol buscarPorId(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + id));
    }

    private List<Permiso> buscarPermisos(List<Integer> idPermisos) {
        List<Permiso> permisos = new ArrayList<>();
        for (Integer idPermiso : idPermisos) {
            permisos.add(permisoRepository.findById(idPermiso)
                    .orElseThrow(() -> new ResourceNotFoundException("Permiso no encontrado: " + idPermiso)));
        }
        return permisos;
    }

    private RolResponse toResponse(Rol rol) {
        List<PermisoResponse> permisos = rol.getPermisos() != null
                ? rol.getPermisos().stream().map(this::toPermisoResponse).toList()
                : List.of();
        return new RolResponse(
                rol.getIdRol(),
                rol.getNombre(),
                rol.getDescripcion(),
                rol.getActivo(),
                permisos);
    }

    private PermisoResponse toPermisoResponse(Permiso permiso) {
        return new PermisoResponse(
                permiso.getIdPermiso(),
                permiso.getNombre(),
                permiso.getDescripcion(),
                permiso.getEntidad(),
                permiso.getAccion());
    }
}
