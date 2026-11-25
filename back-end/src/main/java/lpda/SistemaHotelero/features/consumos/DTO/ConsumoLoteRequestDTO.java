package lpda.SistemaHotelero.features.consumos.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ConsumoLoteRequestDTO {

    @NotNull(message = "La reserva es obligatoria")
    private UUID idReservaExterno;

    @Valid
    @NotEmpty(message = "El carrito debe contener al menos un producto")
    private List<ConsumoItemRequestDTO> consumos;
}