package lpda.SistemaHotelero.features.roles;

import jakarta.persistence.*;
import lombok.*;
import lpda.SistemaHotelero.features.permisos.PermisoEntity;
import lpda.SistemaHotelero.features.roles.enums.Rol;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Rol nombre;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permisos",
            joinColumns = @JoinColumn(name = "id_rol"),
            inverseJoinColumns = @JoinColumn(name = "id_permiso")
    )
    private Set<PermisoEntity> permisos = new HashSet<>();
}