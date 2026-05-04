package lpda.SistemaHotelero.features.huespedes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HuespedRepository extends JpaRepository<HuespedEntity, Long>{

}