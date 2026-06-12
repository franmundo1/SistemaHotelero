package lpda.SistemaHotelero.features.reservas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    Optional<ReservaEntity> findByIdExterno(UUID idExterno);

    boolean existsByIdExterno(UUID idExterno);

    @Query("""
        SELECT COUNT(r) > 0
        FROM ReservaEntity r
        WHERE r.habitacion.idHabitacion = :idHabitacion
        AND r.estado NOT IN ('CANCELADA', 'FINALIZADA')
        AND :fechaEntrada < r.fechaSalida
        AND :fechaSalida > r.fechaEntrada
        """)
    boolean existsReservaSolapada(
            @Param("idHabitacion") Long idHabitacion,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida
    );

    @Query("""
    SELECT COUNT(r) > 0
    FROM ReservaEntity r
    WHERE r.habitacion.idHabitacion = :idHabitacion
    AND r.idReserva <> :idReservaActual
    AND r.estado NOT IN ('CANCELADA', 'FINALIZADA')
    AND :fechaEntrada < r.fechaSalida
    AND :fechaSalida > r.fechaEntrada
    """)
    boolean existsReservaSolapadaExcluyendoReservaActual(
            @Param("idHabitacion") Long idHabitacion,
            @Param("idReservaActual") Long idReservaActual,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida
    );
    List<ReservaEntity> findByEstadoIgnoreCase(String estado);

    long countByEstadoIgnoreCase(String estado);
}