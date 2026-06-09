package lpda.SistemaHotelero.features.checkOut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOutEntity, Long> {

    Optional<CheckOutEntity> findByIdExterno(UUID idExterno);

    boolean existsByReserva_IdExterno(UUID idReservaExterno);

    void deleteByIdExterno(UUID idExterno);
}