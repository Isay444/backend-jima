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

import isa.jima.ventas.dto.request.DarDeBajaRequest;
import isa.jima.ventas.dto.request.TrabajadorRequest;
import isa.jima.ventas.dto.response.TrabajadorResponse;
import isa.jima.ventas.service.TrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    @GetMapping
    @PreAuthorize("hasAuthority('TRABAJADOR_READ')")
    public ResponseEntity<List<TrabajadorResponse>> listar() {
        return ResponseEntity.ok(trabajadorService.listar());
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAuthority('TRABAJADOR_READ')")
    public ResponseEntity<List<TrabajadorResponse>> listarActivos() {
        return ResponseEntity.ok(trabajadorService.listarActivos());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('TRABAJADOR_READ')")
    public ResponseEntity<List<TrabajadorResponse>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(trabajadorService.buscarPorNombre(nombre));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRABAJADOR_READ')")
    public ResponseEntity<TrabajadorResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(trabajadorService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TRABAJADOR_CREATE')")
    public ResponseEntity<TrabajadorResponse> crear(@RequestBody @Valid TrabajadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trabajadorService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRABAJADOR_UPDATE')")
    public ResponseEntity<TrabajadorResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid TrabajadorRequest request) {
        return ResponseEntity.ok(trabajadorService.actualizar(id, request));
    }

    @PatchMapping("/{id}/dar-de-baja")
    @PreAuthorize("hasAuthority('TRABAJADOR_UPDATE')")
    public ResponseEntity<TrabajadorResponse> darDeBaja(
            @PathVariable Integer id,
            @RequestBody @Valid DarDeBajaRequest request) {
        return ResponseEntity.ok(trabajadorService.darDeBaja(id, request.motivo()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRABAJADOR_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        trabajadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
