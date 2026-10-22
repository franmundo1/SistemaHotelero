package lpda.SistemaHotelero.features.reservas.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReservaRequestDTO {

    @NotNull(message = "El huésped es obligatorio")
    private UUID idHuespedExterno;

    @NotBlank(message = "El número de habitación es obligatorio")
    private String numeroHabitacion;

    @NotBlank(message = "El email del usuario creador es obligatorio")
    private String emailUsuarioCreador;

    @NotNull(message = "El canal de reserva es obligatorio")
    private UUID idCanalReservaExterno;

    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDate fechaEntrada;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate fechaSalida;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "La cantidad de personas debe ser al menos 1")
    private Integer cantidadPersonas;

    @DecimalMin(value = "0.00", message = "El anticipo no puede ser negativo")
    private BigDecimal anticipo;

    private String metodoPagoAnticipo;

    private String estado;

    private String observaciones;
}