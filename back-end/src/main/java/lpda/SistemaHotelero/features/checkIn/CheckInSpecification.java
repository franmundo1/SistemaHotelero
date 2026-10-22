package lpda.SistemaHotelero.features.checkIn;

import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class CheckInSpecification {

    // Filtro por ID Externo del Check-In
    public static Specification<CheckInEntity> idExternoEquals(UUID idExterno) {
        return (root, query, criteriaBuilder) ->
                idExterno == null ? null : criteriaBuilder.equal(root.get("idExterno"), idExterno);
    }

    // Filtro por Nombre del Huésped (Navegando a través de la Reserva)
    public static Specification<CheckInEntity> nombreHuespedLike(String nombre) {
        return (root, query, criteriaBuilder) -> {
            if (nombre == null || nombre.isBlank()) return null;

            // Navegamos: CheckIn -> Reserva -> Huesped -> nombre
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("reserva").get("huesped").get("nombre")),
                    "%" + nombre.toLowerCase() + "%"
            );
        };
    }
}