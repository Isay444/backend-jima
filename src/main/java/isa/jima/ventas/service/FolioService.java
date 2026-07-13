package isa.jima.ventas.service;

import isa.jima.ventas.exception.BusinessException;
import isa.jima.ventas.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolioService {

    private final OrdenRepository ordenRepository;

    @Transactional
    public String generarFolio() {
        int maxAttempts = 3;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            String nextFolio = calculateNextFolio();

            // Verificación de unicidad
            if (!ordenRepository.existsByFolio(nextFolio)) {
                return nextFolio;
            }
            log.warn("Intento {} fallido: El folio {} ya existe por concurrencia.", attempt + 1, nextFolio);
        }

        // Fallback dinámico si la contención es extrema
        String fallbackFolio = String.format("%06d", System.currentTimeMillis() % 1_000_000);
        if (ordenRepository.existsByFolio(fallbackFolio)) {
            throw new BusinessException("Imposible generar un folio único en este momento. Por favor, intente nuevamente.");
        }

        return fallbackFolio;
    }

    public String previewFolio() {
        return calculateNextFolio();
    }

    // Lógica interna centralizada para evitar duplicación de código
    private String calculateNextFolio() {
        Long maxNumeric = ordenRepository.findMaxFolioNumerico().orElse(0L);
        if (maxNumeric == 0L) {
            return "001000";
        }
        return String.format("%06d", maxNumeric + 1);
    }
}