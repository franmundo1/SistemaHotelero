package lpda.SistemaHotelero.features.checkIn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Long >, JpaSpecificationExecutor<CheckInEntity> {
    List<CheckInEntity> findByReservas_idReserva(Long idReserva);

    Optional<CheckInEntity>  findById(long id);
    Optional<CheckInEntity>  findByIdExterno (UUID idExterno);

    boolean existByIdExterno (UUID idExterno);

    void deleteByIdExterno(UUID idExterno);



}
