package lpda.SistemaHotelero.features.pagos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {

    List<PagoEntity> findByReserva_IdExterno(UUID idReservaExterno);

    List<PagoEntity> findByUsuario_IdExterno(UUID idUsuarioExterno);

    Optional<PagoEntity> findByIdExterno(UUID idExterno);

    boolean existsByReserva_IdExternoAndTipoPago(UUID idReservaExterno, String tipoPago);
}