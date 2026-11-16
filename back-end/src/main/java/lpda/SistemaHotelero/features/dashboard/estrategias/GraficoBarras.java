package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.GraficoResponseDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
public class GraficoBarras implements GraficoStrategy {

    @Override
    public TipoGrafico tipo() {
        return TipoGrafico.BARRAS;
    }

    // Las barras van de mayor a menor, sin agrupar en "Otros"
    @Override
    public GraficoResponseDTO armar(List<PuntoDato> datos, GraficoRequestDTO consulta) {
        List<PuntoDato> ordenados = datos.stream()
                .sorted(Comparator.comparing(PuntoDato::valor).reversed())
                .toList();

        List<BigDecimal> valores = ordenados.stream().map(PuntoDato::valor).toList();

        return GraficoResponseDTO.builder()
                .tipoGrafico(tipo())
                .titulo(TituloGrafico.armar(consulta))
                .etiquetas(ordenados.stream().map(PuntoDato::etiqueta).toList())
                .valores(valores)
                .total(valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }
}
