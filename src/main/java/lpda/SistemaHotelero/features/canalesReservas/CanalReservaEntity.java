package lpda.SistemaHotelero.features.canalesReservas;
import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name = "canales_reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanalReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_canal")
    private Long idCanal;

    @Column(nullable = false, unique = true)
    private String nombre;
    // HOTEL, BOOKING, WHATSAPP, TELEFONO
}