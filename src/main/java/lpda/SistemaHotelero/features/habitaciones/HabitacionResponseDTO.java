package lpda.SistemaHotelero.features.habitaciones;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionResponseDTO {

    private Long idHabitacion;
    private String numero;
    private String tipo;
    private Integer capacidad;
    private BigDecimal precioPorNoche;
    private String estadoOcupacion;
    private String estadoLimpieza;
    private Boolean activa;
}