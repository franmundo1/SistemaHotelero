package lpda.SistemaHotelero.features.dashboard;

import lombok.Builder;
import lombok.Getter;
import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class GraficoResponseDTO {

    private TipoGrafico tipoGrafico;
    private String titulo;
    private List<String> etiquetas;
    private List<BigDecimal> valores;
    private BigDecimal total;
}
