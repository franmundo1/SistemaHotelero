package lpda.SistemaHotelero.features.checkOut.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CheckOutResponseDTO {

    private UUID idExterno;
    private UUID idReservaExterno;
    private LocalDateTime fechaHoraSalida;
    private String observaciones;
    private String nombreHuesped;
    private String numeroHabitacion;
    private String estadoReserva;
    private String estadoOcupacionHabitacion;
    private String estadoLimpiezaHabitacion;
    private BigDecimal totalEstadia;
    private BigDecimal anticipo;
    private BigDecimal montoFinal;
}