package lpda.SistemaHotelero.features.canalesReservas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanalReservaResponseDTO {

    private UUID idExterno;
    private String tipo;
}
