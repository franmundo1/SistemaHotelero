package lpda.SistemaHotelero.features.pagos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {

    List<PagoEntity> findByReserva_IdReserva(Long idReserva);

    List<PagoEntity> findByUsuario_IdUsuario(Long idUsuario);

    Optional<PagoEntity> findByIdExterno(UUID idExterno);
    List<PagoEntity> findByReserva_IdExterno(UUID idReservaExterno);
    boolean existsByReserva_IdReservaAndTipoPago(Long idReserva, String tipoPago);
}
 