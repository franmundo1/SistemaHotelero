package lpda.SistemaHotelero.features.canalesReservas;

import lpda.SistemaHotelero.features.pagos.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CanalReservaRepository extends JpaRepository<CanalReservaEntity, Long> {
    Optional<CanalReservaEntity> findByIdExterno(UUID idExterno);
    boolean existsByTipo(TipoCanal tipo);

}