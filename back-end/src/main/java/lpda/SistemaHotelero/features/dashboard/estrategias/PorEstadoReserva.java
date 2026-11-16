package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import org.springframework.stereotype.Component;

@Component
public class PorEstadoReserva implements AgrupacionStrategy {

    @Override
    public TipoAgrupacion tipo() {
        return TipoAgrupacion.ESTADO_RESERVA;
    }

    @Override
    public String etiqueta(RegistroGrafico registro) {
        return registro.reserva().getEstado();
    }
}
