package lpda.SistemaHotelero.features.dashboard.estrategias;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReservasMetrica implements MetricaStrategy {

    private final ReservaRepository reservaRepository;

    @Override
    public TipoMetrica tipo() {
        return TipoMetrica.RESERVAS;
    }

    @Override
    public Set<TipoAgrupacion> agrupacionesValidas() {
        return Set.of(TipoAgrupacion.HABITACION, TipoAgrupacion.TIPO_HABITACION, TipoAgrupacion.CANAL, TipoAgrupacion.ESTADO_RESERVA);
    }

    // Cada reserva con entrada en el periodo cuenta 1
    @Override
    public List<PuntoDato> calcular(GraficoRequestDTO consulta, AgrupacionStrategy agrupacion) {
        List<RegistroGrafico> registros = reservaRepository.findByFechaEntradaBetween(consulta.getFechaDesde(), consulta.getFechaHasta())
                .stream()
                .map(reserva -> new RegistroGrafico(reserva, null, null, BigDecimal.ONE))
                .toList();

        return PuntoDato.agrupar(registros, agrupacion);
    }
}
