package lpda.SistemaHotelero.features.huespedes;

import jakarta.persistence.*;
import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;

import java.util.List;

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

    @OneToMany(mappedBy = "huesped")
    private List<ReservaEntity> reservas;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String dni;

    private String telefono;

    private String email;
}