package lpda.SistemaHotelero.features.consumos.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumoResponseDTO {

    private UUID idExterno;

    private UUID idReservaExterno;
    private String numeroHabitacion;

    private UUID idProductoExterno;
    private String producto;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    private LocalDateTime fechaConsumo;
    private String observaciones;
}