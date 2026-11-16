package lpda.SistemaHotelero.features.dashboard.estrategias;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record PuntoDato(String etiqueta, BigDecimal valor) {

    // Suma el valor de los registros que caen en la misma categoria
    public static List<PuntoDato> agrupar(List<RegistroGrafico> registros, AgrupacionStrategy agrupacion) {
        Map<String, BigDecimal> totales = new LinkedHashMap<>();
        registros.forEach(registro ->
                totales.merge(agrupacion.etiqueta(registro), registro.valor(), BigDecimal::add));

        return totales.entrySet().stream()
                .map(total -> new PuntoDato(total.getKey(), total.getValue()))
                .toList();
    }
}
