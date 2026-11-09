package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import org.springframework.stereotype.Component;

@Component
public class PorHabitacion implements AgrupacionStrategy {

    @Override
    public TipoAgrupacion tipo() {
        return TipoAgrupacion.HABITACION;
    }

    @Override
    public String etiqueta(RegistroGrafico registro) {
        return registro.reserva().getHabitacion().getNumero();
    }
}
