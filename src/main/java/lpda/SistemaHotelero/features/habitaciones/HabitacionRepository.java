package lpda.SistemaHotelero.features.habitaciones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<HabitacionEntity, Long> {

    Optional<HabitacionEntity> findByNumero(String numero);

    boolean existsByNumero(String numero);
}