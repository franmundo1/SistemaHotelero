package lpda.SistemaHotelero.features.roles;
import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(nullable = false, unique = true)
    private String nombre;
}
