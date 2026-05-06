package lpda.SistemaHotelero.features.huespedes.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class HuespedResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
}
