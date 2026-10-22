package lpda.SistemaHotelero.features.checkOut.Dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class CheckOutResumenDTO {

    private UUID idReservaExterno;
    private String huesped;
    private String habitacion;

    private BigDecimal totalEstadia;
    private BigDecimal totalConsumos;
    private BigDecimal anticipo;
    private BigDecimal totalFinal;
    private BigDecimal saldoPendiente;
}