package lpda.SistemaHotelero.features.reservas;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.canalesReservas.CanalReservaEntity;
import lpda.SistemaHotelero.features.habitaciones.HabitacionEntity;
import lpda.SistemaHotelero.features.huespedes.HuespedEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_externo", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID idExterno;

    @ManyToOne
    @JoinColumn(name = "id_huesped", nullable = false)
    private HuespedEntity huesped;

    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    private HabitacionEntity habitacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private UsuarioEntity usuarioCreador;

    @ManyToOne
    @JoinColumn(name = "id_canal", nullable = false)
    private CanalReservaEntity canalReserva;


    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas;


    @Column(name = "total_estadia", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalEstadia;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal anticipo;

    @Column(nullable = false)
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "motivo_cancelacion", columnDefinition = "TEXT")
    private String motivoCancelacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();

        if (this.idExterno == null) {
            this.idExterno = UUID.randomUUID();
        }
    }
}