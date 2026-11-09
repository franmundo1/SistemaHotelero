package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;
import org.springframework.stereotype.Component;

// La dona se arma igual que la torta, solo cambia como se dibuja en el front
@Component
public class GraficoDona extends GraficoTorta {

    @Override
    public TipoGrafico tipo() {
        return TipoGrafico.DONA;
    }
}
