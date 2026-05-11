package lpda.SistemaHotelero.features.checkIn.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckInRequestDTO {
    @NotBlank(message = "El ID de la reserva es obligatorio")
    private String reservaExternalId; // Usamos el ID externo de la reserva
    private String observaciones;
}