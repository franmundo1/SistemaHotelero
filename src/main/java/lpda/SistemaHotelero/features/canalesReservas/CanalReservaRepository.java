package lpda.SistemaHotelero.features.canalesReservas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanalReservaRepository extends JpaRepository<CanalReservaEntity, Long> {

    boolean existsByNombrePersona(String nombrePersona);
}
