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

import isa.jima.ventas.dto.request.ServicioRequest;
import isa.jima.ventas.dto.response.ServicioResponse;
import isa.jima.ventas.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    @PreAuthorize("hasAuthority('SERVICIO_READ')")
    public ResponseEntity<List<ServicioResponse>> listar() {
        return ResponseEntity.ok(servicioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_READ')")
    public ResponseEntity<ServicioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SERVICIO_CREATE')")
    public ResponseEntity<ServicioResponse> crear(@RequestBody @Valid ServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_UPDATE')")
    public ResponseEntity<ServicioResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ServicioRequest request) {
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
