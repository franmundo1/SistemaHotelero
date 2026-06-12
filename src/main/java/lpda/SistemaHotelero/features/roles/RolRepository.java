package lpda.SistemaHotelero.features.roles;

import lpda.SistemaHotelero.features.roles.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {

    Optional<RolEntity> findByNombre(Rol nombre);
}