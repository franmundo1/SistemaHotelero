package lpda.SistemaHotelero.features.acompanantes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AcompananteRepository extends JpaRepository<AcompananteEntity, Long> {

    List<AcompananteEntity> findByReserva_IdReserva(Long idReserva);

    Optional<AcompananteEntity> findByIdExterno(UUID idExterno);

    boolean existsByIdExterno(UUID idExterno);

    void deleteByIdExterno(UUID idExterno);
}