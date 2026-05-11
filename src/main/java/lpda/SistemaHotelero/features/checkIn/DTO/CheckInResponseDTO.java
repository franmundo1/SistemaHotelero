package lpda.SistemaHotelero.features.checkIn.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CheckInResponseDTO {
    private UUID id;
    private LocalDateTime fechaHoraEntrada;
    private String observaciones;
    private String reservaId;
    private String nombreHuesped;
}