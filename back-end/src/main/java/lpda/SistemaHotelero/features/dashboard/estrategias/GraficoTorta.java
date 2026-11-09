package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import lpda.SistemaHotelero.features.dashboard.GraficoResponseDTO;
import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class GraficoTorta implements GraficoStrategy {

    @Override
    public TipoGrafico tipo() {
        return TipoGrafico.TORTA;
    }

    // Con muchas porciones la torta no se lee: lo que pasa el limite se suma en "Otros"
    @Override
    public GraficoResponseDTO armar(List<PuntoDato> datos, GraficoRequestDTO consulta) {
        List<PuntoDato> ordenados = datos.stream()
                .sorted(Comparator.comparing(PuntoDato::valor).reversed())
                .toList();

        List<String> etiquetas = new ArrayList<>();
        List<BigDecimal> valores = new ArrayList<>();
        BigDecimal otros = BigDecimal.ZERO;

        for (int i = 0; i < ordenados.size(); i++) {
            if (i < consulta.getLimite()) {
                etiquetas.add(ordenados.get(i).etiqueta());
                valores.add(ordenados.get(i).valor());
            } else {
                otros = otros.add(ordenados.get(i).valor());
            }
        }

        if (otros.compareTo(BigDecimal.ZERO) > 0) {
            etiquetas.add("Otros");
            valores.add(otros);
        }

        return GraficoResponseDTO.builder()
                .tipoGrafico(tipo())
                .titulo(TituloGrafico.armar(consulta))
                .etiquetas(etiquetas)
                .valores(valores)
                .total(valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }
}
