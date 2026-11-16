package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;

// En que categoria cae cada registro
public interface AgrupacionStrategy {

    TipoAgrupacion tipo();

    String etiqueta(RegistroGrafico registro);
}
