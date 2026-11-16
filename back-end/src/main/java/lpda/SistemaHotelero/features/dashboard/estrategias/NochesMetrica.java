package lpda.SistemaHotelero.features.dashboard.estrategias;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NochesMetrica implements MetricaStrategy {

    private final ReservaRepository reservaRepository;

    @Override
    public TipoMetrica tipo() {
        return TipoMetrica.NOCHES_VENDIDAS;
    }

    @Override
    public Set<TipoAgrupacion> agrupacionesValidas() {
        return Set.of(TipoAgrupacion.HABITACION, TipoAgrupacion.TIPO_HABITACION, TipoAgrupacion.CANAL);
    }

    // Noches vendidas: las reservas canceladas no cuentan
    @Override
    public List<PuntoDato> calcular(GraficoRequestDTO consulta, AgrupacionStrategy agrupacion) {
        List<RegistroGrafico> registros = reservaRepository.findByFechaEntradaBetween(consulta.getFechaDesde(), consulta.getFechaHasta())
                .stream()
                .filter(reserva -> !"CANCELADA".equalsIgnoreCase(reserva.getEstado()))
                .map(reserva -> new RegistroGrafico(reserva, null, null,
                        BigDecimal.valueOf(ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida()))))
                .toList();

        return PuntoDato.agrupar(registros, agrupacion);
    }
}
