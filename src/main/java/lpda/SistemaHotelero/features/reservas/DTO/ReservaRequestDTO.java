package lpda.SistemaHotelero.features.reservas.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReservaRequestDTO {

    private UUID idHuespedExterno;
    private String numeroHabitacion;
    private String emailUsuarioCreador;
    private UUID idCanalReservaExterno;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Integer cantidadPersonas;
    private BigDecimal precioPorNoche;
    private BigDecimal totalEstadia;
    private BigDecimal anticipo;
    private String estado;
    private String observaciones;
}
