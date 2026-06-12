package lpda.SistemaHotelero.features.usuarios;

import lpda.SistemaHotelero.features.roles.RolEntity;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioRequestDTO;
import lpda.SistemaHotelero.features.usuarios.DTO.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toResponseDTO(UsuarioEntity usuario) {
        String rol = usuario.getRoles()
                .stream()
                .findFirst()
                .map(r -> r.getNombre().name())
                .orElse(null);

        return UsuarioResponseDTO.builder()
                .idExterno(usuario.getIdExterno())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(rol)
                .activo(usuario.getActivo())
                .build();
    }

    public UsuarioEntity toEntity(UsuarioRequestDTO dto, RolEntity rolEntity, String passwordEncriptada) {
        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncriptada);
        usuario.setActivo(true);
        usuario.getRoles().add(rolEntity);

        return usuario;
    }
}