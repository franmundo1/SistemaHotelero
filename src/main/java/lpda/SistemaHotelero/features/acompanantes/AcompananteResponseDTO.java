package lpda.SistemaHotelero.features.acompanantes;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcompananteResponseDTO {

    private UUID idExterno;

    private Long idReserva;

    private String nombre;

    private String apellido;

    private String dni;
}