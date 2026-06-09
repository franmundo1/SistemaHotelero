package lpda.SistemaHotelero.features.productos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {

    Optional<ProductoEntity> findByIdExterno(UUID idExterno);

    boolean existsByIdExterno(UUID idExterno);

    boolean existsByNombreIgnoreCase(String nombre);
}