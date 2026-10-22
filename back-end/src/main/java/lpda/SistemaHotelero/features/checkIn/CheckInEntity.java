package lpda.SistemaHotelero.features.checkIn;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import lpda.SistemaHotelero.features.usuarios.UsuarioEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "check_in")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CheckInEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_check_in")
    private Long idCheckIn;

    @Column (nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID idExterno;

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

    @PrePersist
    public void ensureEsternalId(){
        if(idExterno == null){
            idExterno = UUID.randomUUID();
        }
    }
}