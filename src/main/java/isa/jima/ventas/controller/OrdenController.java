package isa.jima.ventas.controller;

import isa.jima.ventas.dto.request.OrdenRequest;
import isa.jima.ventas.dto.response.OrdenResponse;
import isa.jima.ventas.service.OrdenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @GetMapping
    @PreAuthorize("hasAuthority('ORDEN_READ')")
    public ResponseEntity<List<OrdenResponse>> listar() {
        return ResponseEntity.ok(ordenService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDEN_READ')")
    public ResponseEntity<OrdenResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenService.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORDEN_CREATE')")
    public ResponseEntity<OrdenResponse> crear(@RequestBody @Valid OrdenRequest request) {
        return new ResponseEntity<>(ordenService.crear(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDEN_UPDATE')")
    public ResponseEntity<OrdenResponse> actualizar(@PathVariable Integer id, @RequestBody @Valid OrdenRequest request) {
        return ResponseEntity.ok(ordenService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDEN_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ordenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAuthority('ORDEN_UPDATE')")
    public ResponseEntity<OrdenResponse> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(ordenService.cancelar(id));
    }

    @GetMapping("/folio-preview")
    @PreAuthorize("hasAuthority('ORDEN_READ')")
    public ResponseEntity<String> previewFolio() {
        return ResponseEntity.ok(ordenService.previewFolio());
    }
}