package lpda.SistemaHotelero.features.huespedes;
import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name = "huespedes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuespedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_huesped")
    private Long idHuesped;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String dni;

    private String telefono;

    private String email;
}