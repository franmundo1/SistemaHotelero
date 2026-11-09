package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.consumos.ConsumoEntity;
import lpda.SistemaHotelero.features.pagos.PagoEntity;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;

import java.math.BigDecimal;

// Un dato que aporta una metrica: la reserva de origen, el pago o consumo si corresponde, y su valor
public record RegistroGrafico(ReservaEntity reserva, PagoEntity pago, ConsumoEntity consumo, BigDecimal valor) {
}
