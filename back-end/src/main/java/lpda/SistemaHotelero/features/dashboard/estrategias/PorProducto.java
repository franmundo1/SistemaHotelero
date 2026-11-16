package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import org.springframework.stereotype.Component;

@Component
public class PorProducto implements AgrupacionStrategy {

    @Override
    public TipoAgrupacion tipo() {
        return TipoAgrupacion.PRODUCTO;
    }

    @Override
    public String etiqueta(RegistroGrafico registro) {
        return registro.consumo().getProducto().getNombre();
    }
}
