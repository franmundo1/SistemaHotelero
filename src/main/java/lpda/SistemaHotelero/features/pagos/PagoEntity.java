package lpda.SistemaHotelero.features.pagos;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private ReservaEntity reserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
    // EFECTIVO, TRANSFERENCIA, TARJETA, MERCADO_PAGO

    @Column(name = "tipo_pago", nullable = false)
    private String tipoPago;
    // ANTICIPO, PARCIAL, FINAL

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}