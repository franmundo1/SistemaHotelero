package lpda.SistemaHotelero.features.huespedes;

import org.springframework.data.jpa.domain.PredicateSpecification;

public class HuespedSpecification {
    public static PredicateSpecification<HuespedEntity> nombreLike(String nombre){

        return (root, cb) -> (nombre == null || nombre.isBlank())
                ? cb.conjunction() // si es nulo no se filtra
                : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");

    }

    public static PredicateSpecification<HuespedEntity> dniEquals (String dni){
        return (root, cb) -> (dni == null || dni.isBlank())
                ? cb.conjunction()
                : cb.equal(root.get("dni"), dni);
    }
}
