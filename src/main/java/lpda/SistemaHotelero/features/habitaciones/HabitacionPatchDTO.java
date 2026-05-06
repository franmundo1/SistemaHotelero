package lpda.SistemaHotelero.features.habitaciones;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionPatchDTO {

    private String numero;
    private String tipo;
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioPorNoche;
    private String estadoOcupacion;
    private String estadoLimpieza;
    private Boolean activa;
}