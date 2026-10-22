package lpda.SistemaHotelero.features.usuarios.DTO;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private UUID idExterno;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private Boolean activo;
}