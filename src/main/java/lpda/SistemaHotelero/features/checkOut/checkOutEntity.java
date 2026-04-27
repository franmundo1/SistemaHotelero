package lpda.SistemaHotelero.features.checkOut;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_out")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class checkOutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_check_out")
    private Long idCheckOut;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private ReservaEntity reserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "total_estadia", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalEstadia;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal anticipo;

    @Column(name = "monto_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoFinal;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}