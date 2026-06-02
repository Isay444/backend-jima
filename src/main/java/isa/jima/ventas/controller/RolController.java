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
import org.springframework.web.bind.annotation.RestController;

import isa.jima.ventas.dto.request.RolRequest;
import isa.jima.ventas.dto.response.PermisoResponse;
import isa.jima.ventas.dto.response.RolResponse;
import isa.jima.ventas.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping("/api/v1/permisos")
    @PreAuthorize("hasAuthority('ROL_READ')")
    public ResponseEntity<List<PermisoResponse>> listarPermisos() {
        return ResponseEntity.ok(rolService.listarPermisos());
    }

    @GetMapping("/api/v1/roles")
    @PreAuthorize("hasAuthority('ROL_READ')")
    public ResponseEntity<List<RolResponse>> listar() {
        return ResponseEntity.ok(rolService.listar());
    }

    @GetMapping("/api/v1/roles/{id}")
    @PreAuthorize("hasAuthority('ROL_READ')")
    public ResponseEntity<RolResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(rolService.obtenerPorId(id));
    }

    @PostMapping("/api/v1/roles")
    @PreAuthorize("hasAuthority('ROL_CREATE')")
    public ResponseEntity<RolResponse> crear(@RequestBody @Valid RolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(request));
    }

    @PutMapping("/api/v1/roles/{id}/permisos")
    @PreAuthorize("hasAuthority('ROL_UPDATE')")
    public ResponseEntity<RolResponse> actualizarPermisos(
            @PathVariable Integer id,
            @RequestBody List<Integer> idPermisos) {
        return ResponseEntity.ok(rolService.actualizarPermisos(id, idPermisos));
    }

    @PatchMapping("/api/v1/roles/{id}/activar-desactivar")
    @PreAuthorize("hasAuthority('ROL_UPDATE')")
    public ResponseEntity<RolResponse> activarDesactivar(@PathVariable Integer id) {
        return ResponseEntity.ok(rolService.activarDesactivar(id));
    }

    @DeleteMapping("/api/v1/roles/{id}")
    @PreAuthorize("hasAuthority('ROL_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
