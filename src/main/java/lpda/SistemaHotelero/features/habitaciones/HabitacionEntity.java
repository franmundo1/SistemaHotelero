package lpda.SistemaHotelero.features.habitaciones;
import jakarta.persistence.*;
        import lombok.*;

        import java.math.BigDecimal;

@Entity
@Table(name = "habitaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long idHabitacion;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String tipo;
    // SIMPLE, DOBLE, TRIPLE, SUITE

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "precio_por_noche", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorNoche;

    @Column(name = "estado_ocupacion", nullable = false)
    private String estadoOcupacion;
    // DISPONIBLE, OCUPADA, MANTENIMIENTO

    @Column(name = "estado_limpieza", nullable = false)
    private String estadoLimpieza;
    // LIMPIA, SUCIA, EN_LIMPIEZA

    @Column(nullable = false)
    private Boolean activa = true;
}