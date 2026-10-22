package lpda.SistemaHotelero.features.reservas.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelarReservaRequestDTO {

    @NotBlank(message = "El motivo de cancelación es obligatorio")
    private String motivoCancelacion;
}