package lpda.SistemaHotelero.features.habitaciones;

import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HabitacionRepository extends JpaRepository<HabitacionEntity, Long> {

    Optional<HabitacionEntity> findByNumero(String numero);

    boolean existsByNumero(String numero);

    Optional<HabitacionEntity> findByIdExterno(UUID idExterno);

    boolean existsByIdExterno(UUID idExterno);

    void deleteByIdExterno(UUID idExterno);

    @Query("""
        SELECT h FROM HabitacionEntity h
        WHERE (:numero IS NULL OR h.numero = :numero)
        AND(:tipo IS NULL OR h.tipo = :tipo)
        AND (:capacidad IS NULL OR h.capacidad >= :capacidad)
        AND (:estadoOcupacion IS NULL OR h.estadoOcupacion = :estadoOcupacion)
        AND (:estadoLimpieza IS NULL OR h.estadoLimpieza = :estadoLimpieza)
        AND (:activa IS NULL OR h.activa = :activa)
        """)
    List<HabitacionEntity> filtrarHabitaciones(
            @Param("numero") String numero,
            @Param("tipo") TipoHabitacion tipo,
            @Param("capacidad") Integer capacidad,
            @Param("estadoOcupacion") EstadoOcupacion estadoOcupacion,
            @Param("estadoLimpieza") EstadoLimpieza estadoLimpieza,
            @Param("activa") Boolean activa
    );

}
