package lpda.SistemaHotelero.features.pagos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDTO {

    private Long idPago;
    private UUID idExterno;

    private Long idReserva;
    private Long idUsuario;
    private String nombreUsuario;

    private BigDecimal monto;
    private String metodoPago;
    private String tipoPago;
    private LocalDateTime fechaPago;
    private String observaciones;
}
