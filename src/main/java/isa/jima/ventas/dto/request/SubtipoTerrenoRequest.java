package isa.jima.ventas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubtipoTerrenoRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull Integer idTipoTerreno) {
}
