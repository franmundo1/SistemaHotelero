package lpda.SistemaHotelero.features.huespedes;

import org.springframework.data.jpa.domain.Specification; // Importamos la estándar

public class HuespedSpecification {

    public static Specification<HuespedEntity> nombreLike(String nombre) {
        // Usamos (root, query, cb) que es la firma estándar de Specification
        return (root, query, cb) -> (nombre == null || nombre.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
    }

    public static Specification<HuespedEntity> dniEquals(String dni) {
        return (root, query, cb) -> (dni == null || dni.isBlank())
                ? cb.conjunction()
                : cb.equal(root.get("dni"), dni);
    }
}