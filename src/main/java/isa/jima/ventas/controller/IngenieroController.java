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

import isa.jima.ventas.dto.request.IngenieroRequest;
import isa.jima.ventas.dto.response.IngenieroResponse;
import isa.jima.ventas.service.IngenieroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ingenieros")
@RequiredArgsConstructor
public class IngenieroController {

    private final IngenieroService ingenieroService;

    @GetMapping
    @PreAuthorize("hasAuthority('INGENIERO_READ')")
    public ResponseEntity<List<IngenieroResponse>> listar() {
        return ResponseEntity.ok(ingenieroService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INGENIERO_READ')")
    public ResponseEntity<IngenieroResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ingenieroService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('INGENIERO_CREATE')")
    public ResponseEntity<IngenieroResponse> crear(@RequestBody @Valid IngenieroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingenieroService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('INGENIERO_UPDATE')")
    public ResponseEntity<IngenieroResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid IngenieroRequest request) {
        return ResponseEntity.ok(ingenieroService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('INGENIERO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ingenieroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
