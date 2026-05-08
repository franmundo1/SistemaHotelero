package lpda.SistemaHotelero.features.habitaciones;

import lombok.*;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionResponseDTO {

    private Long idHabitacion;
    private String numero;
    private TipoHabitacion tipo;
    private Integer capacidad;
    private BigDecimal precioPorNoche;
    private EstadoOcupacion estadoOcupacion;
    private EstadoLimpieza estadoLimpieza;
    private Boolean activa;
}