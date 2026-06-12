package lpda.SistemaHotelero.features.dashboard;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResumenDTO {

    private long habitacionesTotales;
    private long habitacionesDisponibles;
    private long habitacionesOcupadas;
    private long habitacionesMantenimiento;
    private long habitacionesSucias;
    private long habitacionesEnLimpieza;

    private long reservasTotales;
    private long reservasConfirmadas;
    private long reservasEnCurso;
    private long reservasFinalizadas;
    private long reservasCanceladas;

    private long productosActivos;
    private long consumosRegistrados;
}