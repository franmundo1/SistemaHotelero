package lpda.SistemaHotelero.features.reservas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReservaResponseDTO {

    private Long idReserva;

    private String huesped;
    private String habitacion;
    private String usuarioCreador;
    private String canalReserva;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    private BigDecimal totalEstadia;

    private String estado;
}