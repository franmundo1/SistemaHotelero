package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;

import java.util.List;
import java.util.Set;

// Que se mide y como se calcula
public interface MetricaStrategy {

    TipoMetrica tipo();

    Set<TipoAgrupacion> agrupacionesValidas();

    List<PuntoDato> calcular(GraficoRequestDTO consulta, AgrupacionStrategy agrupacion);
}
