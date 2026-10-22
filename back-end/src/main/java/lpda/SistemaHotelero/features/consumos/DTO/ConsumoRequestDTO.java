package lpda.SistemaHotelero.features.consumos.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ConsumoRequestDTO {

    @NotNull(message = "La reserva es obligatoria")
    private UUID idReservaExterno;

    @NotNull(message = "El producto es obligatorio")
    private UUID idProductoExterno;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    private String observaciones;
}