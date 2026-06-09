package lpda.SistemaHotelero.features.acompanantes;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcompananteRequestDTO {

    @NotNull(message = "El ID de la reserva es obligatorio")
    private UUID idReserva;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @Size(max = 20, message = "El DNI no puede superar los 20 caracteres")
    private String dni;
}