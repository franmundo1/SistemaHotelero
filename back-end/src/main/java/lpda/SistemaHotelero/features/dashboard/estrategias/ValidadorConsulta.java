package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.exceptions.BadRequestException;
import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ValidadorConsulta {

    // Periodo maximo para no recorrer toda la base en una sola consulta
    private static final long DIAS_MAXIMOS = 366;

    public void validar(GraficoRequestDTO consulta, MetricaStrategy metrica, GraficoStrategy grafico) {
        if (consulta.getFechaDesde().isAfter(consulta.getFechaHasta())) {
            throw new BadRequestException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        if (ChronoUnit.DAYS.between(consulta.getFechaDesde(), consulta.getFechaHasta()) > DIAS_MAXIMOS) {
            throw new BadRequestException("El período no puede superar un año");
        }

        if (!metrica.agrupacionesValidas().contains(consulta.getAgruparPor())) {
            throw new BadRequestException("La métrica " + consulta.getMetrica()
                    + " no se puede agrupar por " + consulta.getAgruparPor());
        }

        if (grafico == null) {
            throw new BadRequestException("Tipo de gráfico no soportado: " + consulta.getTipoGrafico());
        }
    }
}
