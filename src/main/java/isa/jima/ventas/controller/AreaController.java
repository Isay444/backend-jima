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

import isa.jima.ventas.dto.request.AreaRequest;
import isa.jima.ventas.dto.response.AreaResponse;
import isa.jima.ventas.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @GetMapping
    @PreAuthorize("hasAuthority('AREA_READ')")
    public ResponseEntity<List<AreaResponse>> listar() {
        return ResponseEntity.ok(areaService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('AREA_READ')")
    public ResponseEntity<AreaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(areaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('AREA_CREATE')")
    public ResponseEntity<AreaResponse> crear(@RequestBody @Valid AreaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('AREA_UPDATE')")
    public ResponseEntity<AreaResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AreaRequest request) {
        return ResponseEntity.ok(areaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('AREA_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        areaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
