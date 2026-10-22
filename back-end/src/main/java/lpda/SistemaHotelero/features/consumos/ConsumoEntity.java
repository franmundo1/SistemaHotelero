package lpda.SistemaHotelero.features.consumos;

import jakarta.persistence.*;
import lombok.*;
import lpda.SistemaHotelero.features.productos.ProductoEntity;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    private Long idConsumo;

    @Column(name = "id_externo", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID idExterno;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private ReservaEntity reserva;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "fecha_consumo", nullable = false)
    private LocalDateTime fechaConsumo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @PrePersist
    public void prePersist() {
        if (idExterno == null) {
            idExterno = UUID.randomUUID();
        }

        if (fechaConsumo == null) {
            fechaConsumo = LocalDateTime.now();
        }
    }
}