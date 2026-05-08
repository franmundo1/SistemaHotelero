package lpda.SistemaHotelero.features.habitaciones;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoLimpieza;
import lpda.SistemaHotelero.features.habitaciones.enums.EstadoOcupacion;
import lpda.SistemaHotelero.features.habitaciones.enums.TipoHabitacion;

import java.math.BigDecimal;

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
}