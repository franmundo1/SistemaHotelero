package lpda.SistemaHotelero.features.checkOut.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CheckOutRequestDTO {

    @NotNull(message = "El ID externo de la reserva es obligatorio")
    private UUID idReservaExterno;

    @NotNull(message = "El monto abonado es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto abonado debe ser mayor a 0")
    private BigDecimal montoAbonado;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    private String observaciones;
}