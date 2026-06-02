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

import isa.jima.ventas.dto.request.TipoTerrenoRequest;
import isa.jima.ventas.dto.response.TipoTerrenoResponse;
import isa.jima.ventas.service.TipoTerrenoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipos-terreno")
@RequiredArgsConstructor
public class TipoTerrenoController {

    private final TipoTerrenoService tipoTerrenoService;

    @GetMapping
    @PreAuthorize("hasAuthority('TIPOTERRENO_READ')")
    public ResponseEntity<List<TipoTerrenoResponse>> listar() {
        return ResponseEntity.ok(tipoTerrenoService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOTERRENO_READ')")
    public ResponseEntity<TipoTerrenoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoTerrenoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TIPOTERRENO_CREATE')")
    public ResponseEntity<TipoTerrenoResponse> crear(@RequestBody @Valid TipoTerrenoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoTerrenoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOTERRENO_UPDATE')")
    public ResponseEntity<TipoTerrenoResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid TipoTerrenoRequest request) {
        return ResponseEntity.ok(tipoTerrenoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOTERRENO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoTerrenoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
