package isa.jima.ventas.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import isa.jima.ventas.dto.request.CambiarPasswordRequest;
import isa.jima.ventas.dto.request.UsuarioRequest;
import isa.jima.ventas.dto.request.UsuarioUpdateRequest;
import isa.jima.ventas.dto.response.UsuarioResponse;
import isa.jima.ventas.entity.Rol;
import isa.jima.ventas.entity.Usuario;
import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.exception.ResourceNotFoundException;
import isa.jima.ventas.repository.RolRepository;
import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Integer id) {
        return toResponse(buscarPorId(id));
    }

    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.findByNombre(request.nombre()).isPresent()) {
            throw new BusinessException("El nombre de usuario ya está en uso");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        usuario.setActivo(request.activo());
        usuario.setRol(buscarRol(request.idRol()));
        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse actualizar(Integer id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarPorId(id);
        if (!usuario.getNombre().equals(request.nombre())) {
            usuarioRepository.findByNombre(request.nombre()).ifPresent(existente -> {
                if (!existente.getIdUsuario().equals(id)) {
                    throw new BusinessException("El nombre de usuario ya está en uso");
                }
            });
        }
        usuario.setNombre(request.nombre());
        usuario.setActivo(request.activo());
        usuario.setRol(buscarRol(request.idRol()));
        return toResponse(usuarioRepository.save(usuario));
    }

    public void cambiarPassword(Integer id, CambiarPasswordRequest request) {
        Usuario usuario = buscarPorId(id);
        if (!passwordEncoder.matches(request.contraseniaActual(), usuario.getContrasenia())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }
        usuario.setContrasenia(passwordEncoder.encode(request.contraseniaNueva()));
        usuarioRepository.save(usuario);
    }

    public UsuarioResponse activarDesactivar(Integer id) {
        Usuario usuario = buscarPorId(id);
        if (Boolean.TRUE.equals(usuario.getActivo())
                && ROL_ADMINISTRADOR.equalsIgnoreCase(usuario.getRol().getNombre())) {
            long adminsActivos = usuarioRepository.countByRolNombreAndActivoTrue(ROL_ADMINISTRADOR);
            if (adminsActivos <= 1) {
                throw new BusinessException("No se puede desactivar al último administrador activo del sistema");
            }
        }
        usuario.setActivo(!usuario.getActivo());
        return toResponse(usuarioRepository.save(usuario));
    }

    public void eliminar(Integer id, Integer idUsuarioActual) {
        if (id.equals(idUsuarioActual)) {
            throw new BusinessException("No puedes eliminar tu propio usuario");
        }
        Usuario usuario = buscarPorId(id);
        if (ROL_ADMINISTRADOR.equalsIgnoreCase(usuario.getRol().getNombre())
                && Boolean.TRUE.equals(usuario.getActivo())) {
            long adminsActivos = usuarioRepository.countByRolNombreAndActivoTrue(ROL_ADMINISTRADOR);
            if (adminsActivos <= 1) {
                throw new BusinessException("No se puede eliminar al último administrador activo");
            }
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    private Rol buscarRol(Integer idRol) {
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + idRol));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        Rol rol = usuario.getRol();
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getActivo(),
                rol.getIdRol(),
                rol.getNombre());
    }
}
