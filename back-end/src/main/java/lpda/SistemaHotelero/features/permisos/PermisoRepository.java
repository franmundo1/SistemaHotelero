package lpda.SistemaHotelero.features.permisos;

import lpda.SistemaHotelero.features.roles.enums.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<PermisoEntity, Long> {

    Optional<PermisoEntity> findByPermiso(Permiso permiso);

    boolean existsByPermiso(Permiso permiso);
}