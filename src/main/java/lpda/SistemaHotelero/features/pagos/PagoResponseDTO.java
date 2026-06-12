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

    private UUID idExterno;

    private UUID idReservaExterno;
    private UUID idUsuarioExterno;
    private String nombreUsuario;

    private BigDecimal monto;
    private String metodoPago;
    private String tipoPago;
    private LocalDateTime fechaPago;
    private String observaciones;
}