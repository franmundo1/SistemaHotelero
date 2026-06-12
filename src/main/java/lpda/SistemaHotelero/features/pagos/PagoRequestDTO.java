package lpda.SistemaHotelero.features.pagos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequestDTO {

    @NotNull(message = "El ID de la reserva es obligatorio")
    private UUID idReserva;

    @NotNull(message = "El ID del usuario es obligatorio")
    private UUID idUsuario;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener como máximo 10 enteros y 2 decimales")
    private BigDecimal monto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(
            regexp = "EFECTIVO|TRANSFERENCIA|TARJETA|MERCADO_PAGO",
            message = "El método de pago debe ser: EFECTIVO, TRANSFERENCIA, TARJETA o MERCADO_PAGO"
    )
    private String metodoPago;

    @NotBlank(message = "El tipo de pago es obligatorio")
    @Pattern(
            regexp = "ANTICIPO|PARCIAL|FINAL",
            message = "El tipo de pago debe ser: ANTICIPO, PARCIAL o FINAL"
    )
    private String tipoPago;

    private String observaciones;
}