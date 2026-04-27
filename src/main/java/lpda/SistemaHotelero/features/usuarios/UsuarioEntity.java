package lpda.SistemaHotelero.features.usuarios;
import jakarta.persistence.*;
        import lombok.*;
import lpda.SistemaHotelero.features.roles.RolEntity;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rol;
}