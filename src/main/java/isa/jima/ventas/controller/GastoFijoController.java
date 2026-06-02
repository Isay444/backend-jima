package isa.jima.ventas.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

import isa.jima.ventas.dto.request.GastoFijoRequest;
import isa.jima.ventas.dto.response.GastoFijoResponse;
import isa.jima.ventas.service.GastoFijoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gastos-fijos")
@RequiredArgsConstructor
public class GastoFijoController {

    private final GastoFijoService gastoFijoService;

    @GetMapping
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<List<GastoFijoResponse>> listar() {
        return ResponseEntity.ok(gastoFijoService.listar());
    }

    @GetMapping("/mes-actual")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<List<GastoFijoResponse>> buscarDelMesActual() {
        return ResponseEntity.ok(gastoFijoService.buscarDelMesActual());
    }

    @GetMapping("/resumen")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<Map<String, BigDecimal>> obtenerResumen(
            @RequestParam int mes,
            @RequestParam int anio) {
        return ResponseEntity.ok(gastoFijoService.obtenerResumenMensual(mes, anio));
    }

    @GetMapping("/descripciones")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<List<String>> obtenerDescripciones() {
        return ResponseEntity.ok(gastoFijoService.obtenerDescripcionesUnicas());
    }

    @GetMapping("/con-excedente")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<List<GastoFijoResponse>> buscarConExcedente(
            @RequestParam int mes,
            @RequestParam int anio) {
        return ResponseEntity.ok(gastoFijoService.buscarConExcedente(mes, anio));
    }

    @GetMapping("/con-ahorro")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<List<GastoFijoResponse>> buscarConAhorro(
            @RequestParam int mes,
            @RequestParam int anio) {
        return ResponseEntity.ok(gastoFijoService.buscarConAhorro(mes, anio));
    }

    @GetMapping("/con-resto")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<List<GastoFijoResponse>> buscarConResto(
            @RequestParam int mes,
            @RequestParam int anio) {
        return ResponseEntity.ok(gastoFijoService.buscarConResto(mes, anio));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GASTOFIJO_READ')")
    public ResponseEntity<GastoFijoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(gastoFijoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GASTOFIJO_CREATE')")
    public ResponseEntity<GastoFijoResponse> crear(@RequestBody @Valid GastoFijoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gastoFijoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GASTOFIJO_UPDATE')")
    public ResponseEntity<GastoFijoResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody @Valid GastoFijoRequest request) {
        return ResponseEntity.ok(gastoFijoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/listo-para-pagar")
    @PreAuthorize("hasAuthority('GASTOFIJO_UPDATE')")
    public ResponseEntity<GastoFijoResponse> marcarListoParaPagar(@PathVariable Integer id) {
        return ResponseEntity.ok(gastoFijoService.marcarListoParaPagar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GASTOFIJO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        gastoFijoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
