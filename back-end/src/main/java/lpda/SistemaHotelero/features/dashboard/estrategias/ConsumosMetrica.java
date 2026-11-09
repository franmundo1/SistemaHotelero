package lpda.SistemaHotelero.features.dashboard.estrategias;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.consumos.ConsumoRepository;
import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ConsumosMetrica implements MetricaStrategy {

    private final ConsumoRepository consumoRepository;

    @Override
    public TipoMetrica tipo() {
        return TipoMetrica.CONSUMOS;
    }

    @Override
    public Set<TipoAgrupacion> agrupacionesValidas() {
        return Set.of(TipoAgrupacion.PRODUCTO, TipoAgrupacion.HABITACION);
    }

    @Override
    public List<PuntoDato> calcular(GraficoRequestDTO consulta, AgrupacionStrategy agrupacion) {
        List<RegistroGrafico> registros = consumoRepository.findByFechaConsumoBetween(
                        consulta.getFechaDesde().atStartOfDay(),
                        consulta.getFechaHasta().atTime(LocalTime.MAX))
                .stream()
                .map(consumo -> new RegistroGrafico(consumo.getReserva(), null, consumo, consumo.getSubtotal()))
                .toList();

        return PuntoDato.agrupar(registros, agrupacion);
    }
}
