package isa.jima.ventas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.jima.ventas.dto.request.CambiarPasswordRequest;
import isa.jima.ventas.dto.request.UsuarioRequest;
import isa.jima.ventas.dto.request.UsuarioUpdateRequest;
import isa.jima.ventas.dto.response.UsuarioResponse;
import isa.jima.ventas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIO_READ')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_READ')")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USUARIO_CREATE')")
    public ResponseEntity<UsuarioResponse> crear(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @PatchMapping("/{id}/cambiar-password")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable Integer id,
            @RequestBody @Valid CambiarPasswordRequest request) {
        usuarioService.cambiarPassword(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar-desactivar")
    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    public ResponseEntity<UsuarioResponse> activarDesactivar(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.activarDesactivar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_DELETE')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id,
            @RequestParam Integer idUsuarioActual) {
        usuarioService.eliminar(id, idUsuarioActual);
        return ResponseEntity.noContent().build();
    }
}
