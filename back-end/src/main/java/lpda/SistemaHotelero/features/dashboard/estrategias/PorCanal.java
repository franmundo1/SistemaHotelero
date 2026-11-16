package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import org.springframework.stereotype.Component;

@Component
public class PorCanal implements AgrupacionStrategy {

    @Override
    public TipoAgrupacion tipo() {
        return TipoAgrupacion.CANAL;
    }

    @Override
    public String etiqueta(RegistroGrafico registro) {
        return registro.reserva().getCanalReserva().getTipo().name();
    }
}
