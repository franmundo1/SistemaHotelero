package lpda.SistemaHotelero.features.checkIn;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class checkInEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_check_in")
    private Long idCheckIn;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private ReservaEntity reserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}