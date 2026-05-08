package lpda.SistemaHotelero.features.habitaciones;

import jakarta.validation.constraints.*;
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
public class HabitacionRequestDTO {

    @NotBlank(message = "El número de habitación es obligatorio")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "El número de habitación debe ser un número positivo")
    private String numero;

    @NotBlank(message = "El tipo de habitación es obligatorio")
    private TipoHabitacion tipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser como mínimo 1")
    @Max(value = 3, message = "La capacidad máxima permitida es 3")
    private Integer capacidad;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioPorNoche;

    @NotBlank(message = "El estado de ocupación es obligatorio")
    private EstadoOcupacion estadoOcupacion;

    @NotBlank(message = "El estado de limpieza es obligatorio")
    private EstadoLimpieza estadoLimpieza;

    @Builder.Default
    private Boolean activa = true;
}