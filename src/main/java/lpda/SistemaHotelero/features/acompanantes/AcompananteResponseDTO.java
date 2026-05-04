package lpda.SistemaHotelero.features.acompanantes;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcompananteResponseDTO {

    private Long idAcompanante;

    private Long idReserva;

    private String nombre;

    private String apellido;

    private String dni;

}