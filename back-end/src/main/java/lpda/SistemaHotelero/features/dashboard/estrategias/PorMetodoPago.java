package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import org.springframework.stereotype.Component;

@Component
public class PorMetodoPago implements AgrupacionStrategy {

    @Override
    public TipoAgrupacion tipo() {
        return TipoAgrupacion.METODO_PAGO;
    }

    @Override
    public String etiqueta(RegistroGrafico registro) {
        return registro.pago().getMetodoPago();
    }
}
