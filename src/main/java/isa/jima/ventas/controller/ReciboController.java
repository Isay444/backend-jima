package isa.jima.ventas.controller;

import isa.jima.ventas.dto.request.ReciboRequest;
import isa.jima.ventas.dto.response.ReciboResponse;
import isa.jima.ventas.service.ReciboService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recibos")
@RequiredArgsConstructor
public class ReciboController {

    private final ReciboService reciboService;

    @GetMapping("/orden/{idOrden}")
    @PreAuthorize("hasAuthority('RECIBO_READ')")
    public ResponseEntity<List<ReciboResponse>> listarPorOrden(@PathVariable Integer idOrden) {
        return ResponseEntity.ok(reciboService.listarPorOrden(idOrden));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RECIBO_READ')")
    public ResponseEntity<ReciboResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(reciboService.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RECIBO_CREATE')")
    public ResponseEntity<ReciboResponse> crear(@RequestBody @Valid ReciboRequest request) {
        return new ResponseEntity<>(reciboService.crear(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RECIBO_DELETE')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reciboService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}