package lpda.SistemaHotelero.features.permisos;


import jakarta.persistence.*;
import lombok.*;
import lpda.SistemaHotelero.features.roles.enums.Permiso;

@Entity
@Table(name = "permisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermisoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long idPermiso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Permiso permiso;
}