package lpda.SistemaHotelero.features.acompanantes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AcompananteRepository extends JpaRepository<AcompananteEntity, Long> {

    List<AcompananteEntity> findByReserva_IdReserva(UUID idExterno);

    Optional<AcompananteEntity> findByIdExterno(UUID idExterno);

    boolean existsByIdExterno(UUID idExterno);

    boolean existsByReserva_IdExternoAndDni(UUID idReservaExterno, String dni);

    boolean existsByReserva_IdExternoAndDniAndIdExternoNot(
            UUID idReservaExterno,
            String dni,
            UUID idExterno
    );

    long countByReserva_IdReserva(Long idReserva);

    void deleteByIdExterno(UUID idExterno);
}