package lpda.SistemaHotelero.features.habitaciones;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
public class HabitacionPatchDTO {

    @Pattern(regexp = "^[1-9][0-9]*$", message = "El número de habitación debe ser un número positivo")
    private String numero;

    private TipoHabitacion tipo;

    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Min(value = 1, message = "La capacidad debe ser como mínimo 1")
    @Max(value = 3, message = "La capacidad máxima permitida es 3")
    private Integer capacidad;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioPorNoche;
    private EstadoOcupacion estadoOcupacion;
    private EstadoLimpieza estadoLimpieza;
    private Boolean activa;
}