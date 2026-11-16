package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.GraficoResponseDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;

import java.util.List;

// Como se arma la respuesta segun el tipo de grafico
public interface GraficoStrategy {

    TipoGrafico tipo();

    GraficoResponseDTO armar(List<PuntoDato> datos, GraficoRequestDTO consulta);
}
