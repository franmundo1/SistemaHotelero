package lpda.SistemaHotelero.features.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByIdExterno(UUID idExterno);

    boolean existsByEmail(String email);

    boolean existsByIdExterno(UUID idExterno);

    List<UsuarioEntity> findByActivoTrue();
}