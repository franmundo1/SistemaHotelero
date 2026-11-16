package lpda.SistemaHotelero.features.dashboard.estrategias;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;
import lpda.SistemaHotelero.features.pagos.PagoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class IngresosMetrica implements MetricaStrategy {

    private final PagoRepository pagoRepository;

    @Override
    public TipoMetrica tipo() {
        return TipoMetrica.INGRESOS;
    }

    @Override
    public Set<TipoAgrupacion> agrupacionesValidas() {
        return Set.of(TipoAgrupacion.HABITACION, TipoAgrupacion.TIPO_HABITACION, TipoAgrupacion.CANAL, TipoAgrupacion.METODO_PAGO);
    }

    // Los ingresos salen de los pagos registrados en el periodo, no del precio de la habitacion
    @Override
    public List<PuntoDato> calcular(GraficoRequestDTO consulta, AgrupacionStrategy agrupacion) {
        List<RegistroGrafico> registros = pagoRepository.findByFechaPagoBetween(
                        consulta.getFechaDesde().atStartOfDay(),
                        consulta.getFechaHasta().atTime(LocalTime.MAX))
                .stream()
                .map(pago -> new RegistroGrafico(pago.getReserva(), pago, null, pago.getMonto()))
                .toList();

        return PuntoDato.agrupar(registros, agrupacion);
    }
}
