package isa.jima.ventas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.jima.ventas.dto.request.NominaRequest;
import isa.jima.ventas.dto.response.NominaResponse;
import isa.jima.ventas.dto.response.ResumenNominaResponse;
import isa.jima.ventas.service.NominaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/nominas")
@RequiredArgsConstructor
public class NominaController {

    private final NominaService nominaService;

    @GetMapping
    @PreAuthorize("hasAuthority('NOMINA_READ')")
    public ResponseEntity<List<NominaResponse>> listar() {
        return ResponseEntity.ok(nominaService.listar());
    }

    @GetMapping("/rango")
    @PreAuthorize("hasAuthority('NOMINA_READ')")
    public ResponseEntity<List<NominaResponse>> buscarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(nominaService.buscarPorRangoFecha(inicio, fin));
    }

    @GetMapping("/resumen")
    @PreAuthorize("hasAuthority('NOMINA_READ')")
    public ResponseEntity<ResumenNominaResponse> calcularResumen(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(nominaService.calcularResumen(inicio, fin));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('NOMINA_READ')")
    public ResponseEntity<NominaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(nominaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('NOMINA_CREATE')")
    public ResponseEntity<NominaResponse> crear(@RequestBody @Valid NominaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nominaService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('NOMINA_UPDATE')")
    public ResponseEntity<NominaResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid NominaRequest request) {
        return ResponseEntity.ok(nominaService.actualizar(id, request));
    }

    @PatchMapping("/{id}/marcar-pagado")
    @PreAuthorize("hasAuthority('NOMINA_UPDATE')")
    public ResponseEntity<NominaResponse> marcarComoPagado(@PathVariable Integer id) {
        return ResponseEntity.ok(nominaService.marcarComoPagado(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('NOMINA_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        nominaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
