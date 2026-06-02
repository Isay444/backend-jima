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

import isa.jima.ventas.dto.request.SubtipoTerrenoRequest;
import isa.jima.ventas.dto.response.SubtipoTerrenoResponse;
import isa.jima.ventas.service.SubtipoTerrenoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/subtipos-terreno")
@RequiredArgsConstructor
public class SubtipoTerrenoController {

    private final SubtipoTerrenoService subtipoTerrenoService;

    @GetMapping
    @PreAuthorize("hasAuthority('SUBTIPOTERRENO_READ')")
    public ResponseEntity<List<SubtipoTerrenoResponse>> listar() {
        return ResponseEntity.ok(subtipoTerrenoService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBTIPOTERRENO_READ')")
    public ResponseEntity<SubtipoTerrenoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(subtipoTerrenoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUBTIPOTERRENO_CREATE')")
    public ResponseEntity<SubtipoTerrenoResponse> crear(@RequestBody @Valid SubtipoTerrenoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subtipoTerrenoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBTIPOTERRENO_UPDATE')")
    public ResponseEntity<SubtipoTerrenoResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid SubtipoTerrenoRequest request) {
        return ResponseEntity.ok(subtipoTerrenoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBTIPOTERRENO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        subtipoTerrenoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
