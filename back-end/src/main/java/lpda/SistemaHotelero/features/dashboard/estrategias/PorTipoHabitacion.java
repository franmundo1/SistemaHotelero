package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import org.springframework.stereotype.Component;

@Component
public class PorTipoHabitacion implements AgrupacionStrategy {

    @Override
    public TipoAgrupacion tipo() {
        return TipoAgrupacion.TIPO_HABITACION;
    }

    @Override
    public String etiqueta(RegistroGrafico registro) {
        return registro.reserva().getHabitacion().getTipo().name();
    }
}
