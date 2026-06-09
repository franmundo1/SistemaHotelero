package lpda.SistemaHotelero.features.consumos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsumoRepository extends JpaRepository<ConsumoEntity, Long> {

    Optional<ConsumoEntity> findByIdExterno(UUID idExterno);

    List<ConsumoEntity> findByReserva_IdExterno(UUID idReservaExterno);
}