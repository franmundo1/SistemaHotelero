package lpda.SistemaHotelero.features.huespedes;

import jakarta.persistence.*;
import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "huespedes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuespedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_huesped")
    private Long idHuesped;

    @Column(name = "id_externo", nullable = false, unique = true, length = 36)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID idExterno;

    @OneToMany(mappedBy = "huesped")
    private List<ReservaEntity> reservas;


    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String dni;

    private String telefono;

    private String email;

    @PrePersist
    public void ensureExternalId(){
        if(idExterno == null){
            idExterno = UUID.randomUUID();
        }
    }
}