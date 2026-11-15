package lpda.SistemaHotelero.features.huespedes;

import lpda.SistemaHotelero.features.acompanantes.AcompananteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface HuespedRepository extends JpaRepository<HuespedEntity, Long>,
        JpaSpecificationExecutor<HuespedEntity> {
    List<HuespedEntity> findByReservas_IdReserva(Long idReserva);

    Optional<HuespedEntity> findByIdExterno(UUID idExterno);
    Optional<HuespedEntity> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByDniAndIdExternoNot(String dni, UUID idExterno);
    boolean existsByIdExterno(UUID idExterno);

    void deleteByIdExterno(UUID idExterno);

}

