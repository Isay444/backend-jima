package isa.jima.ventas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isa.jima.ventas.dto.request.PuestoRequest;
import isa.jima.ventas.dto.response.PuestoResponse;
import isa.jima.ventas.service.PuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/puestos")
@RequiredArgsConstructor
public class PuestoController {

    private final PuestoService puestoService;

    @GetMapping
    @PreAuthorize("hasAuthority('PUESTO_READ')")
    public ResponseEntity<List<PuestoResponse>> listar() {
        return ResponseEntity.ok(puestoService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PUESTO_READ')")
    public ResponseEntity<PuestoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(puestoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PUESTO_CREATE')")
    public ResponseEntity<PuestoResponse> crear(@RequestBody @Valid PuestoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PUESTO_UPDATE')")
    public ResponseEntity<PuestoResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PuestoRequest request) {
        return ResponseEntity.ok(puestoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PUESTO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        puestoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
