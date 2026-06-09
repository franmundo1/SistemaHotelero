package lpda.SistemaHotelero.features.habitaciones;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "habitaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long idHabitacion;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(unique = true, nullable = false, name = "id_externo")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID idExterno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoHabitacion tipo;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "precio_por_noche", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorNoche;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_ocupacion", nullable = false)
    private EstadoOcupacion estadoOcupacion;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado_limpieza",nullable = false )
    private EstadoLimpieza estadoLimpieza;

    @Column(nullable = false)
    private Boolean activa = true;

    @PrePersist
    void onSave() {
        if (idExterno == null) {
            idExterno = UUID.randomUUID();
        }
    }
}