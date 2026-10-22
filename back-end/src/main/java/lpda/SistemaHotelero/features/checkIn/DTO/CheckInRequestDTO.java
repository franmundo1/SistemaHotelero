package lpda.SistemaHotelero.features.checkIn.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckInRequestDTO {

    @NotNull(message = "El ID externo de la reserva es obligatorio")
    private UUID idReservaExterno;

    private String observaciones;
}