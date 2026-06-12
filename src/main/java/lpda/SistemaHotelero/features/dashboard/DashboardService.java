package lpda.SistemaHotelero.features.dashboard;

import lombok.RequiredArgsConstructor;
import lpda.SistemaHotelero.features.consumos.ConsumoRepository;
import lpda.SistemaHotelero.features.habitaciones.HabitacionRepository;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.productos.ProductoRepository;
import lpda.SistemaHotelero.features.reservas.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final ProductoRepository productoRepository;
    private final ConsumoRepository consumoRepository;

    @Transactional(readOnly = true)
    public DashboardResumenDTO obtenerResumen() {
        return DashboardResumenDTO.builder()
                .habitacionesTotales(habitacionRepository.countByActivaTrue())
                .habitacionesDisponibles(habitacionRepository.countByEstadoOcupacion(EstadoOcupacion.DISPONIBLE))
                .habitacionesOcupadas(habitacionRepository.countByEstadoOcupacion(EstadoOcupacion.OCUPADA))
                .habitacionesMantenimiento(habitacionRepository.countByEstadoOcupacion(EstadoOcupacion.MANTENIMIENTO))
                .habitacionesSucias(habitacionRepository.countByEstadoLimpieza(EstadoLimpieza.SUCIA))
                .habitacionesEnLimpieza(habitacionRepository.countByEstadoLimpieza(EstadoLimpieza.EN_LIMPIEZA))

                .reservasTotales(reservaRepository.count())
                .reservasConfirmadas(reservaRepository.countByEstadoIgnoreCase("CONFIRMADA"))
                .reservasEnCurso(reservaRepository.countByEstadoIgnoreCase("EN_CURSO"))
                .reservasFinalizadas(reservaRepository.countByEstadoIgnoreCase("FINALIZADA"))
                .reservasCanceladas(reservaRepository.countByEstadoIgnoreCase("CANCELADA"))

                .productosActivos(productoRepository.countByActivoTrue())
                .consumosRegistrados(consumoRepository.count())
                .build();
    }
}