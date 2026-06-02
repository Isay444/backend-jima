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

import isa.jima.ventas.dto.request.ZonaEjidalRequest;
import isa.jima.ventas.dto.response.ZonaEjidalResponse;
import isa.jima.ventas.service.ZonaEjidalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/zonas-ejidales")
@RequiredArgsConstructor
public class ZonaEjidalController {

    private final ZonaEjidalService zonaEjidalService;

    @GetMapping
    @PreAuthorize("hasAuthority('ZONAEJIDAL_READ')")
    public ResponseEntity<List<ZonaEjidalResponse>> listar() {
        return ResponseEntity.ok(zonaEjidalService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ZONAEJIDAL_READ')")
    public ResponseEntity<ZonaEjidalResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(zonaEjidalService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ZONAEJIDAL_CREATE')")
    public ResponseEntity<ZonaEjidalResponse> crear(@RequestBody @Valid ZonaEjidalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zonaEjidalService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ZONAEJIDAL_UPDATE')")
    public ResponseEntity<ZonaEjidalResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ZonaEjidalRequest request) {
        return ResponseEntity.ok(zonaEjidalService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ZONAEJIDAL_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        zonaEjidalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
