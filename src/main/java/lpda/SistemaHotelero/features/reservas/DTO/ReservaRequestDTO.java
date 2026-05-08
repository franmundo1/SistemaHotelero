package lpda.SistemaHotelero.features.reservas.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ReservaRequestDTO {

    private Long idHuesped;
    private Long idHabitacion;
    private Long idUsuarioCreador;
    private Long idCanalReserva;

    private String codigoReservaExterna;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Integer cantidadPersonas;
    private BigDecimal precioPorNoche;
    private BigDecimal totalEstadia;
    private BigDecimal anticipo;
    private String estado;
    private String observaciones;
}
