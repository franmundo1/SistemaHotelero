package lpda.SistemaHotelero.config;

import lpda.SistemaHotelero.features.permisos.PermisoEntity;
import lpda.SistemaHotelero.features.permisos.PermisoRepository;
import lpda.SistemaHotelero.features.roles.RolEntity;
import lpda.SistemaHotelero.features.roles.RolRepository;
import lpda.SistemaHotelero.features.roles.enums.Permiso;
import lpda.SistemaHotelero.features.roles.enums.Rol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    public DataInitializer(RolRepository rolRepository, PermisoRepository permisoRepository) {
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
    }

    @Override
    public void run(String... args) {
        crearPermisos();

        asignarPermisos(Rol.ROLE_ADMIN, Set.of(Permiso.values()));

        asignarPermisos(Rol.ROLE_RECEPCIONISTA, Set.of(
                Permiso.VER_HABITACIONES,
                Permiso.CAMBIAR_ESTADO_OCUPACION,
                Permiso.VER_RESERVAS,
                Permiso.CREAR_RESERVA,
                Permiso.ACTUALIZAR_RESERVA,
                Permiso.CANCELAR_RESERVA,
                Permiso.VER_HUESPEDES,
                Permiso.CREAR_HUESPED,
                Permiso.ACTUALIZAR_HUESPED,
                Permiso.REALIZAR_CHECK_IN,
                Permiso.REALIZAR_CHECK_OUT,
                Permiso.VER_PAGOS,
                Permiso.REGISTRAR_PAGO
        ));

        asignarPermisos(Rol.ROLE_LIMPIEZA, Set.of(
                Permiso.CAMBIAR_ESTADO_LIMPIEZA
        ));
    }

    private void crearPermisos() {
        Arrays.stream(Permiso.values()).forEach(permiso -> {
            if (!permisoRepository.existsByPermiso(permiso)) {
                PermisoEntity entity = new PermisoEntity();
                entity.setPermiso(permiso);
                permisoRepository.save(entity);
            }
        });
    }

    private void asignarPermisos(Rol rol, Set<Permiso> permisos) {
        RolEntity rolEntity = rolRepository.findByNombre(rol)
                .orElseGet(() -> {
                    RolEntity nuevoRol = new RolEntity();
                    nuevoRol.setNombre(rol);
                    return rolRepository.save(nuevoRol);
                });

        rolEntity.getPermisos().clear();

        permisos.forEach(permiso -> {
            PermisoEntity permisoEntity = permisoRepository.findByPermiso(permiso)
                    .orElseThrow();
            rolEntity.getPermisos().add(permisoEntity);
        });

        rolRepository.save(rolEntity);
    }
}