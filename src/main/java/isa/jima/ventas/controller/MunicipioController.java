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

import isa.jima.ventas.dto.request.MunicipioRequest;
import isa.jima.ventas.dto.response.MunicipioResponse;
import isa.jima.ventas.service.MunicipioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/municipios")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioService municipioService;

    @GetMapping
    @PreAuthorize("hasAuthority('MUNICIPIO_READ')")
    public ResponseEntity<List<MunicipioResponse>> listar() {
        return ResponseEntity.ok(municipioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MUNICIPIO_READ')")
    public ResponseEntity<MunicipioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(municipioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MUNICIPIO_CREATE')")
    public ResponseEntity<MunicipioResponse> crear(@RequestBody @Valid MunicipioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(municipioService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MUNICIPIO_UPDATE')")
    public ResponseEntity<MunicipioResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid MunicipioRequest request) {
        return ResponseEntity.ok(municipioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MUNICIPIO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        municipioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
