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

import isa.jima.ventas.dto.request.TipoServicioRequest;
import isa.jima.ventas.dto.response.TipoServicioResponse;
import isa.jima.ventas.service.TipoServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipos-servicio")
@RequiredArgsConstructor
public class TipoServicioController {

    private final TipoServicioService tipoServicioService;

    @GetMapping
    @PreAuthorize("hasAuthority('TIPOSERVICIO_READ')")
    public ResponseEntity<List<TipoServicioResponse>> listar() {
        return ResponseEntity.ok(tipoServicioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOSERVICIO_READ')")
    public ResponseEntity<TipoServicioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoServicioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TIPOSERVICIO_CREATE')")
    public ResponseEntity<TipoServicioResponse> crear(@RequestBody @Valid TipoServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoServicioService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOSERVICIO_UPDATE')")
    public ResponseEntity<TipoServicioResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid TipoServicioRequest request) {
        return ResponseEntity.ok(tipoServicioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TIPOSERVICIO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoServicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
