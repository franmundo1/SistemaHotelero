package lpda.SistemaHotelero.features.dashboard.estrategias;

import lpda.SistemaHotelero.features.dashboard.GraficoRequestDTO;

import java.time.format.DateTimeFormatter;
import java.util.Map;

// Arma un titulo legible, por ejemplo: Ingresos por habitacion (01/10/2026 al 31/10/2026)
public final class TituloGrafico {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final Map<String, String> TEXTOS = Map.of(
            "INGRESOS", "Ingresos",
            "RESERVAS", "Reservas",
            "NOCHES_VENDIDAS", "Noches vendidas",
            "CONSUMOS", "Consumos",
            "HABITACION", "habitación",
            "TIPO_HABITACION", "tipo de habitación",
            "CANAL", "canal",
            "ESTADO_RESERVA", "estado de la reserva",
            "METODO_PAGO", "método de pago",
            "PRODUCTO", "producto"
    );

    private TituloGrafico() {
    }

    public static String armar(GraficoRequestDTO consulta) {
        return TEXTOS.get(consulta.getMetrica().name())
                + " por " + TEXTOS.get(consulta.getAgruparPor().name())
                + " (" + consulta.getFechaDesde().format(FORMATO)
                + " al " + consulta.getFechaHasta().format(FORMATO) + ")";
    }
}
