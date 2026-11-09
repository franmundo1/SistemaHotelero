package lpda.SistemaHotelero.features.dashboard;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.dashboard.estrategias.AgrupacionStrategy;
import lpda.SistemaHotelero.features.dashboard.estrategias.EstrategiasRegistry;
import lpda.SistemaHotelero.features.dashboard.estrategias.GraficoStrategy;
import lpda.SistemaHotelero.features.dashboard.estrategias.MetricaStrategy;
import lpda.SistemaHotelero.features.dashboard.estrategias.ValidadorConsulta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardDinamicoService {

    private final EstrategiasRegistry registry;
    private final ValidadorConsulta validador;

    // El servicio solo combina estrategias, no conoce ninguna metrica ni grafico en particular
    @Transactional(readOnly = true)
    public GraficoResponseDTO generar(GraficoRequestDTO consulta) {
        MetricaStrategy metrica = registry.metrica(consulta.getMetrica());
        AgrupacionStrategy agrupacion = registry.agrupacion(consulta.getAgruparPor());
        GraficoStrategy grafico = registry.grafico(consulta.getTipoGrafico());

        validador.validar(consulta, metrica, grafico);
        return grafico.armar(metrica.calcular(consulta, agrupacion), consulta);
    }
}
