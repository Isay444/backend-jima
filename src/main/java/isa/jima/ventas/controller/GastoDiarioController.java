package isa.jima.ventas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.jima.ventas.dto.request.GastoDiarioRequest;
import isa.jima.ventas.dto.response.GastoDiarioResponse;
import isa.jima.ventas.service.GastoDiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gastos-diarios")
@RequiredArgsConstructor
public class GastoDiarioController {

    private final GastoDiarioService gastoDiarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('GASTODIARIO_READ')")
    public ResponseEntity<List<GastoDiarioResponse>> listar() {
        return ResponseEntity.ok(gastoDiarioService.listar());
    }

    @GetMapping("/rango")
    @PreAuthorize("hasAuthority('GASTODIARIO_READ')")
    public ResponseEntity<List<GastoDiarioResponse>> buscarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(gastoDiarioService.buscarPorRangoFecha(inicio, fin));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GASTODIARIO_READ')")
    public ResponseEntity<GastoDiarioResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(gastoDiarioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GASTODIARIO_CREATE')")
    public ResponseEntity<GastoDiarioResponse> crear(@RequestBody @Valid GastoDiarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gastoDiarioService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GASTODIARIO_UPDATE')")
    public ResponseEntity<GastoDiarioResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid GastoDiarioRequest request) {
        return ResponseEntity.ok(gastoDiarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GASTODIARIO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        gastoDiarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
