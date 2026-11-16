package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Spring inyecta todas las implementaciones y se indexan por su tipo.
// Agregar una estrategia nueva es crear una clase, sin tocar este registro.
@Component
public class EstrategiasRegistry {

    private final Map<TipoMetrica, MetricaStrategy> metricas;
    private final Map<TipoAgrupacion, AgrupacionStrategy> agrupaciones;
    private final Map<TipoGrafico, GraficoStrategy> graficos;

    public EstrategiasRegistry(List<MetricaStrategy> metricas,
                               List<AgrupacionStrategy> agrupaciones,
                               List<GraficoStrategy> graficos) {
        this.metricas = metricas.stream().collect(Collectors.toMap(MetricaStrategy::tipo, Function.identity()));
        this.agrupaciones = agrupaciones.stream().collect(Collectors.toMap(AgrupacionStrategy::tipo, Function.identity()));
        this.graficos = graficos.stream().collect(Collectors.toMap(GraficoStrategy::tipo, Function.identity()));
    }

    public MetricaStrategy metrica(TipoMetrica tipo) {
        return metricas.get(tipo);
    }

    public AgrupacionStrategy agrupacion(TipoAgrupacion tipo) {
        return agrupaciones.get(tipo);
    }

    public GraficoStrategy grafico(TipoGrafico tipo) {
        return graficos.get(tipo);
    }
}
