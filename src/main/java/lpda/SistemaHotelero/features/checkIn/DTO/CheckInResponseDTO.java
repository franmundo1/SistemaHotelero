package lpda.SistemaHotelero.features.checkIn.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CheckInResponseDTO {

    private UUID idExterno;
    private UUID idReservaExterno;
    private LocalDateTime fechaHoraEntrada;
    private String observaciones;
    private String nombreHuesped;
    private String numeroHabitacion;
    private String estadoReserva;
    private String estadoOcupacionHabitacion;
}