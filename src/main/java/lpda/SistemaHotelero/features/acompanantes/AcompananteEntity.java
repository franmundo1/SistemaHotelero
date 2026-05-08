package lpda.SistemaHotelero.features.acompanantes;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.reservas.ReservaEntity;

import java.util.UUID;

@Entity
@Table(name = "acompanantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcompananteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acompanante")
    private Long idAcompanante;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private ReservaEntity reserva;

    @Column(unique = true, nullable = false, name="id_externo")
    private UUID idExterno;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    private String dni;

    @PrePersist
    void onSave(){
        if(idExterno == null)
            idExterno = UUID.randomUUID();
    }
}