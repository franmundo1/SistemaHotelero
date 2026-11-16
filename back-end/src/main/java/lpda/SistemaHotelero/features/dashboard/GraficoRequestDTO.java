package lpda.SistemaHotelero.features.dashboard;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lpda.SistemaHotelero.features.dashboard.enums.TipoAgrupacion;
import lpda.SistemaHotelero.features.dashboard.enums.TipoGrafico;
import lpda.SistemaHotelero.features.dashboard.enums.TipoMetrica;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class GraficoRequestDTO {

    @NotNull(message = "El tipo de gráfico es obligatorio")
    private TipoGrafico tipoGrafico;

    @NotNull(message = "La métrica es obligatoria")
    private TipoMetrica metrica;

    @NotNull(message = "La agrupación es obligatoria")
    private TipoAgrupacion agruparPor;

    @NotNull(message = "La fecha desde es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaDesde;

    @NotNull(message = "La fecha hasta es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaHasta;

    // Cantidad maxima de porciones antes de juntar el resto en "Otros"
    @Min(value = 1, message = "El límite debe ser al menos 1")
    @Max(value = 10, message = "El límite no puede superar 10")
    private Integer limite = 5;
}
