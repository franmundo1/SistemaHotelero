package lpda.SistemaHotelero.features.acompanantes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcompananteRepository extends JpaRepository<AcompananteEntity, Long> {

    List<AcompananteEntity> findByReserva_IdReserva(Long idReserva);
}
