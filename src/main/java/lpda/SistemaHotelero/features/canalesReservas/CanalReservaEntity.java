package lpda.SistemaHotelero.features.canalesReservas;
import jakarta.persistence.*;
        import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    private TipoCanal tipo;

    @Column(unique = true, nullable = false, name="id_externo")
    private UUID idExterno;

    @PrePersist
    public void prePersist() {
        if(idExterno == null) idExterno = UUID.randomUUID();

    }
}