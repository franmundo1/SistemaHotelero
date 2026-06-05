package lpda.SistemaHotelero.features.habitaciones;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitacionLimpiezaResponseDTO {

    private String numero;
    private String estadoLimpieza;
}