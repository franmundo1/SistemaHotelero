package lpda.SistemaHotelero.features.huespedes.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class HuespedResponseDTO {

    private Long id;

    private UUID idExterno;

    private String nombre;

    private String apellido;

    private String dni;

    private String email;

}
